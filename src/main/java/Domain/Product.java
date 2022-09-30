package Domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productNummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<OVChipkaart> ovChipkaartList = new ArrayList<>();

    public Product(int productNummer, String naam, String beschrijving, double prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public void addOvChip(OVChipkaart ovChip){
        if (!ovChipkaartList.contains(ovChip)) {
            ovChipkaartList.add(ovChip);
            ovChip.addProduct(this);
        }
    }

    public void removeOvChip(OVChipkaart ovChip){
        if (!ovChipkaartList.contains(ovChip)) {
            ovChipkaartList.remove(ovChip);
            ovChip.removeProduct(this);
        }
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaartList() {
        return ovChipkaartList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productNummer == product.productNummer;
    }

    @Override
    public String toString() {
        return "{" +
                "#" + productNummer +
                " " + naam +
                " " + beschrijving +
                " " + prijs +
                " " + ovChipkaartList +
                '}';
    }
}
