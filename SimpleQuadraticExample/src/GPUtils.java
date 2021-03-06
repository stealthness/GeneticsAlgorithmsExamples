import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GPUtils {


    static BiFunction<Double[], List<Node>, Double> identity = ((inputs, nodes) -> nodes.get(0).calculate(inputs));
    static BiFunction<Double[], List<Node>, Double> abs = ((inputs, nodes) -> -nodes.get(0).calculate(inputs));
    static BiFunction<Double[], List<Node>, Double> reciprocal = ((inputs, nodes) -> 1/nodes.get(0).calculate(inputs));
    static BiFunction<Double[], List<Node>, Double> sin  = ((inputs, nodes) ->Math.sin(nodes.get(0).calculate(inputs)));


    static BiFunction<Double[], List<Node>, Double> add =
            ((inputs,nodes) -> nodes.stream()
                    .mapToDouble(node -> node.calculate(inputs))
                    .reduce(0.0, Double::sum));

    static BiFunction<Double[], List<Node>, Double> min =
            ((inputs,nodes) -> nodes.stream()
                    .mapToDouble(node -> node.calculate(inputs)).min().getAsDouble());

    static BiFunction<Double[], List<Node>, Double> multiply =
            ((inputs, nodes) -> nodes.get(0).calculate(inputs)*nodes.get(1).calculate(inputs));

    static BiFunction<Double[], List<Node>, Double> subtract =
            ((inputs,nodes) -> nodes.get(0).calculate(inputs)-nodes.get(1).calculate(inputs));

    static BiFunction<Double[], List<Node>, Double> divide =
            ((inputs,nodes) -> {
                Double divisor = nodes.get(1).calculate(inputs);
                Double numerator = nodes.get(0).calculate(inputs);
                return numerator/(( divisor== 0.0)?1.0:divisor);
            });

    // Mutate Functions

    static BiFunction<List<Node>, Double, Node> mutateIndex1 = (nodes,mutateRate) -> {
        if (Math.random() > mutateRate){
            return nodes.get(0);
        }else{
            if (nodes.get(0).size()==1){
                return nodes.get(1).clone();
            }else{
                Node mutatedNode =  nodes.get(0).clone();
                ((FunctionNode)mutatedNode).replaceSubtreeAt(1,nodes.get(1));
                return mutatedNode;
            }
        }
    };

    static BiFunction<List<String>,Integer[], List<Node>> crossoverAt = (parentList,indexes) -> {
        var parentNode0 = NodeUtils.createNodeFromString(parentList.get(0));
        var parentNode1 = NodeUtils.createNodeFromString(parentList.get(1));
        Node child0 = parentNode0.clone();
        ((FunctionNode)child0).replaceSubtreeAt(indexes[0],parentNode1.getSubtree(indexes[1]).get());
        Node child1 = parentNode1.clone();
        ((FunctionNode)child1).replaceSubtreeAt(indexes[1],parentNode0.getSubtree(indexes[0]).get());
        return Arrays.asList(child0,child1);
    };


    static BiFunction<List<Node>, Double, Node> mutateRandomIndex = (nodes,mutateRate) -> {
        if (Math.random() > mutateRate){
            return nodes.get(0);
        }else{
            if (nodes.get(0).size()==1){  // only a single terminal node
                return nodes.get(1).clone();
            }else{
                int randomIndex = new Random().nextInt(nodes.get(0).size());
                Node mutatedNode =  nodes.get(0).clone();
                ((FunctionNode)mutatedNode).replaceSubtreeAt(randomIndex ,nodes.get(1));
                return mutatedNode;
            }
        }
    };

    // static methods


    public static GPFunction getGPFunction(String functionString){
        switch (functionString) {
            case "+":
                return new GPMultiFunction(add, functionString);
            case "/":
                return new GPBiFunction(divide, functionString);
            case "*":
                return new GPMultiFunction(multiply, functionString);
            case "-":
                return new GPBiFunction(subtract, functionString);
            default:
                GPFunction function = null;
                for (Field field : GPUtils.class.getDeclaredFields()) {

                    if (functionString.equals(field.getName())) {
                        try {
                            switch (functionString) {
                                case "abs", "reciprocal", "identity" -> {
                                    Class<?> functionClass = Class.forName(GPSingleFunction.class.getName());
                                    Constructor<?> functionConstructor = functionClass.getDeclaredConstructors()[0];
                                    function = (GPFunction) functionConstructor.newInstance(GPUtils.class.getDeclaredField(functionString).get(null), functionString);
                                }
                                default -> {
                                    Class<?> functionClass = Class.forName(GPMultiFunction.class.getName());
                                    Constructor<?> functionConstructor = functionClass.getDeclaredConstructors()[0];
                                    function = (GPFunction) functionConstructor.newInstance(GPUtils.class.getDeclaredField(functionString).get(null), functionString);
                                }
                            }
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                return function;
        }
    }

    static Function<Population, Individual> selectUniformRandomIndividual = population ->{
        int r = new Random().nextInt(population.size());
        return population.getIndividuals().get(r);
    };

    static Function<Population,Individual> selectWeightedParent = population -> {
        // Get individuals
        final var individuals = population.getIndividuals();

        // Spin roulette wheel
        final var rouletteWheelPosition = Math.random() * population.getSumOfFitness();

        // Find parent
        var spinWheel = 0.0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return individuals.get(individuals.size()-1);
    };


    public static double evaluateFitness(List<Node> nodes, double[] doubles) {
        double sum = 0;
        int count = 0;
        for (double point = doubles[0]; point <= doubles[1]; point = point + doubles[2]){
            var inputs = new Double[]{point};
//            System.out.println(String.format("node : ,%s   calculation : %f",nodes.get(0).toClojureString(),nodes.get(0).calculate(inputs)));
 //           System.out.println(String.format("node : ,%s   calculation : %f",nodes.get(1).toClojureString(),nodes.get(1).calculate(inputs)));
//            System.out.println();
            sum += Math.abs(nodes.get(0).calculate(inputs) - nodes.get(1).calculate(inputs));
            count++;
//            System.out.println(String.format("point: %f  sum : %f",point,sum));
        }
        // return adjusted fitness  1 / 1+(raw fitness)
        return 1 / (1+(sum/count));
    }
}
