package data;
// tipo di dato (outlook o temperature o wind)



abstract class Attribute {
    private String name;
    private int index;

    protected Attribute(String name, int index) {
        // System.out.println("Attribute constructed");
        this.name = name;
        this.index = index;
    }

    public Attribute() {
        this.name = "";
        this.index = -1;
    }

    String getName() {
        return this.name;
    }

    int getIndex() {
        return this.index;
    }

    public String toString() {
        return name;
    }

}