package controllers;

public final class Kitten {
    private final String name;

    public Kitten(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kitten kitten = (Kitten) o;

        if (name != null ? !name.equals(kitten.name) : kitten.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}