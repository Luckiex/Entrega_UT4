package dam2;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);

    public static void main(String[] args)
    {
        menu();
    }

    private static void menu()
    {
        int op = 1;
        while (op!=0)
        {
            System.out.println("""
            
            BIBLIOTECA
        ----------------------------------------------------
        1.- Listar los libros
        2.- Insertar un libro
        3.- Insertar varios libros
        4.- Actualizar información de un libro
        5.- Buscador de libros
        6.- Eliminar un libro
        0.- Cerrar programa
            """);

            op = scanner.nextInt();

            if ((op>-1)&&(op<8))
            {
                switch (op)
                {
                    case 0: System.exit(0);
                    case 1: listaLibros(); break;
                    case 2: insertaLibro(); break;
                    case 3: insertaLibros(); break;
                    case 4: actualizaLibro(); break;
                    case 5: buscaLibros(); break;
                    case 6: eliminaLibro();
                }
            }
            else
                System.out.println("Número no válido, por favor, ponga un número entre el 0 y el 7");
        }

    }

    private static void listaLibros()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            FindIterable<Document> libros = mco.find();

            for (Document libro : libros) {
                System.out.println(libro.toJson());
            }
            System.out.println("");
        }
        catch (MongoException e)
        { e.printStackTrace(); }
    }

    private static void insertaLibro()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            System.out.print("Dime el id de tu nuevo libro: ");
            long id = scanner.nextInt();

            Document filtro = new Document("_id", id);
            while (mco.countDocuments(filtro)>0)
            {
                System.out.println("El id introducido ya está en uso");
                System.out.print("\nDime otro id: ");
                id = scanner.nextInt();
                filtro = new Document("_id", id);
            }
            Libro libro = pideDatos(id);

            Document documento = new Document("_id",libro.get_id())
                    .append("titulo",libro.getTitulo())
                    .append("autor",libro.getAutor())
                    .append("precio",libro.getPrecio())
                    .append("genero",libro.getGenero());

            mco.insertOne(documento);
        }
        catch (MongoException e)
        { e.printStackTrace(); }
    }

    private static void insertaLibros()
    {
        System.out.print("¿Cuántos libros quieres insertar? ");
        int num = scanner.nextInt();
        for (int i = 0; i<num; i++)
            insertaLibro();
    }

    private static void actualizaLibro()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            System.out.print("Dime el id del libro a modificar: ");
            long id = scanner.nextInt();
            Document documento = new Document("_id", id);

            if (mco.countDocuments(documento)>0)
            {
                Document filtro = new Document("_id", id);

                System.out.println("Ahora los datos a actualizar");

                Libro libro = pideDatos(id);

                Document docMod = new Document("titulo",libro.getTitulo())
                        .append("autor",libro.getAutor())
                        .append("precio",libro.getPrecio())
                        .append("genero",libro.getGenero());

                mco.replaceOne(filtro,docMod);
            }
            else
                System.out.println("No hay libros registrados con ese id");

        }
        catch (MongoException e)
        { e.printStackTrace(); }
    }

    private static void buscaLibros()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            int opcion;
            Document documento = null;
            System.out.println("""
                    
                    ¿Qué criterio quieres seguir para buscarlo/s?
                    1.- Su id
                    2.- Su título
                    3.- Su autor
                    4.- Los más caros a X€
                    5.- Los más baratos a X€
                    """);

            opcion = scanner.nextInt();
            if ((opcion>0)&&(opcion<6))
            {
                switch (opcion)
                {
                    case 1:
                        System.out.print("Dime el id de tu libro: ");
                        long id = scanner.nextInt();
                        documento = new Document("_id", id);
                        break;

                    case 2:
                        System.out.print("Dime el titulo de tu libro: ");
                        String titulo = scanner2.nextLine();
                        documento = new Document("titulo", titulo);
                        break;

                    case 3:
                        System.out.print("Dime el autor de los libros que buscas: ");
                        String autor = scanner2.nextLine();
                        documento = new Document("autor", autor);
                        break;

                    case 4:
                        System.out.print("¿Cuál es tu presupuesto mínimo? ");
                        int precioMin = scanner.nextInt();
                        documento = new Document("precio", new Document("$gte", precioMin));
                        break;

                    case 5: System.out.print("¿Cuál es tu presupuesto máximo? ");
                        int precioMax = scanner.nextInt();
                        documento = new Document("precio", new Document("$lte", precioMax));
                        break;
                }
            }
            else
                System.out.println("Número no válido, por favor, ponga un número entre el 1 y el 5");

            if (mco.countDocuments(documento)>0)
            {
                FindIterable<Document> libros = mco.find(documento);

                for (Document libro : libros) {
                    System.out.println(libro.toJson());
                }
                System.out.println("");
            }
            else
                System.out.println("No hay libros registrados con ese parámetro");

        }
        catch (MongoException e)
        { e.printStackTrace(); }
    }

    private static void eliminaLibro()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            System.out.print("Dime el id del libro a borrar: ");
            int id = scanner.nextInt();

            mco.deleteOne(new Document("_id", id));
        }
        catch (MongoException e)
        { e.printStackTrace(); }
    }

    private static Libro pideDatos(Long id)
    {
        System.out.print("\nDime el título: ");
        String titulo = scanner2.nextLine();

        System.out.print("\nDime el autor: ");
        String autor = scanner2.nextLine();

        System.out.print("\nDime el precio: ");
        Double precio = scanner.nextDouble();

        System.out.print("\n¿Cuántos géneros tiene? ");
        int num = scanner.nextInt();
        List<String> generos = new ArrayList<String>() {};
        String genero;

        if(num>=1)
        {
            System.out.println("\nDime un género: ");
            genero = scanner2.nextLine();
            generos.add(genero);

            if(num>1)
            {
                for (int i = 1; i < num; i++)
                {
                    System.out.print("\nDime un género: ");
                    genero = scanner2.nextLine();
                    generos.add(genero);
                }
            }
        }
        Libro libro = new Libro(id,titulo,autor,precio,generos);
        return libro;
    }
}