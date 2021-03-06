import lombok.Data;

/**
 * Variable Node will return the index value of the inputs
 */
@Data
public class VariableNode implements Node {

    private final int index;

    @Override
    public Double calculate(Double[] inputs) {
        return inputs[index];
    }

    @Override
    public String toClojureString() {
        return "x"+index;
    }

    @Override
    public Node clone() {
        return new VariableNode(index);
    }



}
