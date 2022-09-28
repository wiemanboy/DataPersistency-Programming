package Domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaartNummer;
    private Date gelidgTot;
    private int klasse;
    private double saldo;
    private int reizigerId;
    private List<Product> products = new ArrayList<>();

    public OVChipkaart(int kaartNummer, Date gelidgTot, int klasse, double saldo , int reizigerId) {
        this.kaartNummer = kaartNummer;
        this.gelidgTot = gelidgTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerId = reizigerId;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGelidgTot() {
        return gelidgTot;
    }

    public void setGelidgTot(Date gelidgTot) {
        this.gelidgTot = gelidgTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    @Override
    public String toString() {
        return "domain.OVChipkaart{" +
                "#" + kaartNummer +
                " " + gelidgTot +
                " " + klasse +
                " $" + saldo +
                " " + reizigerId +
                '}';
    }
}
