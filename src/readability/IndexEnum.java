package readability;

public enum IndexEnum {
    ARI("Automated Readabiliyu Index"),
    FK("Flesch–Kincaid"),
    SMOG("Simple Measure of Gobbledygook"),
    CL("Coleman–Liau index"),
    ALL("all");

    String name;

    IndexEnum(String name){
        this.name = name;
    }
}
