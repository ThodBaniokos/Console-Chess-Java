package enums;

public enum Color {
    WHITE,
    BLACK;

    public Color nextColor() {

        if (this == WHITE) {

            return BLACK;
        }

        return WHITE;
    }
}
