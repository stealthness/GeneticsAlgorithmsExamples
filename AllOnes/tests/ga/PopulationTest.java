package ga;

import ga.Individual;
import ga.Population;

class PopulationTest {

    private static final int POP_SIZE = 20;
    private static final int CHROMOSOME_SIZE = 8;
    private static final double TOL = 0.01;
    private static final int MAX_COUNT = 10000;
    private Population population;
    private Individual emptyIndividual,evenIndividual,completeIndividual;
    private int[] emptyChromosome,evenChromosome,completeChromosome;

//
//
//    @BeforeEach
//    void setUp(){
//        emptyChromosome = new int[]{0,0,0,0,0,0,0,0};
//        evenChromosome = new int[]{1,1,1,0,0,0,1,0};
//        completeChromosome = new int[]{1,1,1,1,1,1,1,1};
//
//        emptyIndividual = new ga.Individual(emptyChromosome);
//        evenIndividual = new ga.Individual(evenChromosome);
//        completeIndividual = new ga.Individual(completeChromosome);
//
//        population = new ga.Population(POP_SIZE);
//    }
//
//    @Test
//    void testSize(){
//        assertEquals(POP_SIZE,population.size());
//    }
//
//    @Test
//    void testInitializePopulation(){
//        population.initialize(CHROMOSOME_SIZE);
//        Arrays.stream(population.getIndividuals()).forEach(individual -> {
//            assertEquals(CHROMOSOME_SIZE,individual.size());
//            assertTrue(individual.getChromosome().stream()
//                    .allMatch(gene -> gene == 0 || gene == 1));
//        });
//    }
//
//
//    @Test
//    void testRandomIndividual(){
//        var count = 0;
//        var noOnes = 0;
//        while (count++ < MAX_COUNT){
//            var size = (int)(Math.random()*10)+2;
//            int[] randomArray = createRandomArray(Optional.of(size));
//            StringBuilder sb = new StringBuilder();
//            Arrays.stream(randomArray).forEach(sb::append);
//            count +=size;
//            var individual = new ga.Individual(randomArray);
//            assertEquals(size, individual.size(), "size");
//            assertEquals(sb.toString(),individual.toString(),"string");
//            noOnes += Arrays.stream(randomArray).filter(x -> x==1).count();
//        }
//
//        // check that within 2 SD of mean
//        assertTrue(Math.abs(count - 2*noOnes) < MAX_COUNT/5," error:"+(Math.abs(count - 2*noOnes)) );
//    }
//
//    @Test
//    void testSetPopulationAllToEmpty(){
//        setAllIndividualsInPopulationTo(emptyIndividual);
//        assertTrue(Arrays.stream(population.getIndividuals())
//                .allMatch(individual -> emptyIndividual.toString().equals(individual.toString())));
//
//    }
//
//    @Test
//    void testSetPopulationAllToComplete(){
//        setAllIndividualsInPopulationTo(completeIndividual);
//        assertTrue(Arrays.stream(population.getIndividuals())
//                .allMatch(individual -> completeIndividual.toString().equals(individual.toString())));
//    }
//
//    @Test
//    void testGetFitnessForCompleteIndividuals(){
//        setAllIndividualsInPopulationTo(completeIndividual);
//        population.evaluateFitness();
//        assertEquals(1.0, population.getFitness(),TOL);
//    }
//
//    @Test
//    void testGetFitnessForEvenIndividuals(){
//        setAllIndividualsInPopulationTo(evenIndividual);
//        population.evaluateFitness();
//        assertEquals(0.5, population.getFitness(),TOL);
//    }
//    @Test
//    void testGetFitnessForEmptyIndividuals(){
//        setAllIndividualsInPopulationTo(emptyIndividual);
//        population.evaluateFitness();
//        assertEquals(0.0, population.getFitness(),TOL);
//    }
//
//
//    @Test
//    void testSetGeneToOneForEmptyPopulation(){
//
//        int count = 0;
//        // repeat test a numebr of times
//        while(count++<100){
//            // create an empty individuals
//            setAllIndividualsInPopulationTo(emptyIndividual);
//            // select a random gene in individuals
//            var randomIndividualIndex = (int)(Math.random()*POP_SIZE);
//            var randomGeneIndex = (int)(Math.random()* CHROMOSOME_SIZE);
//            // change random gene in individuals to 1
//            population.setGene(randomIndividualIndex,randomGeneIndex,1);
//            // assert all but the random gene is 0
//            assertTrue(IntStream.range(0,POP_SIZE)
//                    .allMatch(i -> IntStream.range(0, CHROMOSOME_SIZE)
//                            .filter(gene -> i != randomIndividualIndex && gene != randomGeneIndex)
//                            .allMatch(gene -> population.getGene(i,gene) == 0)));
//            // assert that the random gene is 1
//            assertEquals(1,population.getGene(randomIndividualIndex,randomGeneIndex));
//        }
//
//    }
//
//
//    @Test
//    void testEvenPopulationWithSillyLastFitnessFunctions(){
//        setAllIndividualsInPopulationTo(evenIndividual);
//        population.evaluateFitness();
//        assertEquals(0.5,population.getFitness(),TOL);
//
//        population.evaluateFitness(ga.GAUtils.sillyLastGeneFitness);
//        assertEquals(0.0,population.getFitness(),TOL);
//    }
//
//    @Test
//    void testEvenPopulationWithSillyFirstFitnessFunctions(){
//
//        setAllIndividualsInPopulationTo(evenIndividual);
//        population.evaluateFitness();
//        assertEquals(0.5,population.getFitness(),TOL);
//
//        population.evaluateFitness(ga.GAUtils.sillyFirstGeneFitness);
//        assertEquals(1.0,population.getFitness(),TOL);
//    }
//
//    @Test
//    void testPopulationWithIncreasingIndividualsGetMeanFitnessReturnsCorrectIndividual(){
//        population = setMixedIndividualPopulation();
//        population.evaluateFitness(ga.GAUtils.getMeanGeneFitness);
//        assertEquals(0.0,population.getIndividuals()[1].getFitness());
//        assertEquals(1.0,population.getIndividuals()[7].getFitness());
//        assertEquals(0.1,population.getIndividuals()[0].getFitness());
//        assertEquals(0.5,population.getIndividuals()[9].getFitness());
//        assertEquals(0.9,population.getIndividuals()[5].getFitness());
//    }
//
//
//    @Test
//    void testClone(){
//        setAllIndividualsInPopulationTo(emptyIndividual);
//        var newPopulation = population.clone();
//        population.setGene(0,0,1);
//        // after changing individuals, no side affect on newPopulation
//        assertTrue(Arrays.stream(newPopulation.getIndividuals()).allMatch(individual -> emptyIndividual.equals(individual)));
//    }
//
//
//
//
//
//    // helper methods
//
//    private void setAllIndividualsInPopulationTo(ga.Individual individual){
//        population.initialize(CHROMOSOME_SIZE);
//        IntStream.range(0,POP_SIZE).forEach(i -> population.setIndividual(i,individual));
//    }
//
//    static public ga.Population setMixedIndividualPopulation(){
//        ga.Population pop = new ga.Population(10);
//        pop.initialize(10);
//        pop.setIndividual(0,new ga.Individual(new int[]{0,0,1,0,0,0,0,0,0,0})); //1
//        pop.setIndividual(1,new ga.Individual(new int[]{0,0,0,0,0,0,0,0,0,0})); //0
//        pop.setIndividual(2,new ga.Individual(new int[]{0,0,0,1,0,0,1,0,0,0})); //2
//        pop.setIndividual(3,new ga.Individual(new int[]{0,0,1,0,1,1,1,1,1,1})); //7
//        pop.setIndividual(4,new ga.Individual(new int[]{0,1,0,1,0,0,1,0,0,0})); //3
//        pop.setIndividual(5,new ga.Individual(new int[]{1,1,1,0,1,1,1,1,1,1})); //9
//        pop.setIndividual(6,new ga.Individual(new int[]{1,0,1,1,1,1,0,1,1,1})); //8
//        pop.setIndividual(7,new ga.Individual(new int[]{1,1,1,1,1,1,1,1,1,1})); //10
//        pop.setIndividual(8,new ga.Individual(new int[]{1,0,0,0,1,1,1,0,0,0})); //4
//        pop.setIndividual(9,new ga.Individual(new int[]{1,1,0,0,1,1,1,0,0,0})); //5
//        return pop;
//    }
//
//    public int[] createRandomArray(Optional<Integer> size){
//        int[] randomArray = new int[(size.orElse(CHROMOSOME_SIZE))];
//        IntStream.range(0,size.orElse(CHROMOSOME_SIZE)).forEach(i->{
//            randomArray[i] = (Math.random()<0.5)?1:0;
//        });
//        return randomArray;
//    }
//
//    // test on private method
//
//    @Test
//    void testRandomArray(){
//        var count = 0;
//        var noOnes = 0;
//        while (count++ < MAX_COUNT){
//            var size = (int)(Math.random()*10)+2;
//            int[] randomArray = createRandomArray(Optional.of(size));
//            StringBuilder sb = new StringBuilder();
//            Arrays.stream(randomArray).forEach(sb::append);
//            count += size;
//            noOnes += Arrays.stream(randomArray).filter(x -> x==1).count();
//        }
//        // check that within 2 SD of mean
//        assertTrue(Math.abs(count - 2*noOnes) < MAX_COUNT/5," error:"+(Math.abs(count - 2*noOnes)) );
//    }


}