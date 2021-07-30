package androidx.constraintLayout.desktop.layout;

public class ConstraintLayoutState {
    public ConstraintsState constraints = new ConstraintsState();
    public GuidelinesState guidelines = new GuidelinesState();

    public void clear() {
        constraints.clear();
        guidelines.clear();
    }

    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append(guidelines.serialize());
        builder.append(constraints.serialize());
        return builder.toString();
    }
}
