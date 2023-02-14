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
        1.- Listar libro
        2.- Insertar libro
        3.- Insertar varios libros
        3.- Listar los libros existentes con precio mayor a X
        4.- Actualizar precio libros
        5.- Eliminar libro
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
                    case 4: listaLibrosPrecio(); break;
                    case 5: actualizaLibro(); break;
                    case 6: buscaLibros(); break;
                    case 7: eliminaLibro();
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
        }
        catch (MongoException e)
        {
            e.printStackTrace();
        }
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
                System.out.print("Dime el id otro id: ");
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
        {
            e.printStackTrace();
        }
    }

    private static void insertaLibros()
    {
        System.out.print("¿Cuántos libros quieres insertar? ");
        int num = scanner.nextInt();
        for (int i = 0; i<num; i++)
            insertaLibro();
    }

    private static void listaLibrosPrecio()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");
            System.out.println("ID: ");
            int id = scanner.nextInt();
        }
        catch (MongoException e)
        {
            e.printStackTrace();
        }
    }

    private static void actualizaLibro()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

            System.out.print("Dime el id del libro a modificar: ");
            long id = scanner.nextInt();
            Document filtro = new Document("_id", id);

            System.out.println("Ahora los datos a actualizar");

            Libro libro = pideDatos(id);

            Document docMod = new Document("_id",libro.get_id())
                    .append("titulo",libro.getTitulo())
                    .append("autor",libro.getAutor())
                    .append("precio",libro.getPrecio())
                    .append("genero",libro.getGenero());

            mco.updateOne(filtro,docMod);
        }
        catch (MongoException e)
        {
            e.printStackTrace();
        }
    }

    private static void buscaLibros()
    {
        try(MongoClient mc = new MongoClient("localhost",27017))
        {
            MongoDatabase mdb = mc.getDatabase("lucas");
            MongoCollection<Document> mco = mdb.getCollection("libros");

        }
        catch (MongoException e)
        {
            e.printStackTrace();
        }
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
        {
            e.printStackTrace();
        }
    }

    private static Libro pideDatos(Long id)
    {
        System.out.print("Dime el título: ");
        String titulo = scanner2.nextLine();

        System.out.print("Dime el autor: ");
        String autor = scanner2.nextLine();

        System.out.print("Dime el precio: ");
        Double precio = scanner.nextDouble();

        System.out.print("¿Cuántos géneros tiene?");
        int num = scanner.nextInt();
        List<String> generos = new ArrayList<String>() {
        };
        String genero;

        System.out.println("Dime un género: ");
        genero = scanner2.next();
        generos.add(genero);

        if(num>1)
        {
            for (int i = 1; i < num; i++)
            {
                System.out.print("Dime un género: ");
                genero = scanner2.next();
                generos.add(genero);
            }
        }
        Libro libro = new Libro(id,titulo,autor,precio,generos);
        return libro;
    }
}