package dam2;

import java.util.ArrayList;
import java.util.List;

public class Libro
{
    private long _id;
    private String titulo;
    private String autor;
    private double precio;
    private List<String> genero;

    public Libro() { genero = new ArrayList<String>(); }

    public Libro(long _id, String titulo, String autor, double precio, List<String> genero)
    {
        this._id = _id;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        if (genero != null)
            this.genero = genero;
        else
            this.genero = new ArrayList<String>();
    }

    public long get_id() { return _id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public double getPrecio() { return precio; }
    public List<String> getGenero() { return genero; }

    public void set_id(long _id) { this._id = _id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setGenero(List<String> genero) { this.genero = genero; }

    public void anadirCategoria(String c) { genero.add(c); }

    @Override
    public String toString() {
        return "Libro{" +
                "_id=" + _id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", precio=" + precio +
                ", categoria=" + genero +
                '}';
    }
}
