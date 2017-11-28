package jrl.acdat.acdat_tema2;

public class Conversion {

    private double cambio;

    public double getCambio() {
        return this.cambio;
    }

    public Conversion()
    {
        this.cambio = 0.943948347;
    }

    public Conversion(double cambio)
    {
        this.cambio = cambio;
    }

    public String convertirADolares(String cantidad) {
        double valor = Double.parseDouble(cantidad) / cambio;
        return String.format("%.2f", valor);
    }

    public String convertirAEuros(String cantidad) {
        double valor = Double.parseDouble(cantidad) * cambio;
        return String.format("%.2f", valor);
    }
}
