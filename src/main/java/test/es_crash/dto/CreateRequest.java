package test.es_crash.dto;

public class CreateRequest {
    private int nbItem;
    private int texteLength;

    public int getNbItem() {
        return nbItem;
    }

    public void setNbItem(int nbItem) {
        this.nbItem = nbItem;
    }

    public int getTexteLength() {
        return texteLength;
    }

    public void setTexteLength(int texteLength) {
        this.texteLength = texteLength;
    }
}
