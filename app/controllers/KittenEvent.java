package controllers;

public final class KittenEvent {

    private final String type;
    private final String text;

    public KittenEvent(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
