package RL;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Agent{
    private double learning_rate;
    private double discount_factor;
    private double epsilon;
    private double[][] qtable;
    private Environnement environnement;

    //this method returns the action that has a maximum value in a state.
    private static int maxState(double[] line, Environnement environnement, int state){
        ArrayList<Integer> possible = environnement.possible(state);
        int ret = possible.get(0);
        for (int j:possible) {
            if(line[j]>line[ret]){
                ret = j;
            }
        }
        return ret;
    }

    //choose an action using the epsilon-greedy policy
    private int chooseAction(double[][] qtable, int state, Environnement environnement){
        double[] probs = new double[4];
        for (int i = 0; i < 4; i++){
            probs[i] = 0;
        }
        ArrayList<Integer> possible = environnement.possible(state);
        int max = possible.get(0);
        for (Integer integer : possible) {
            if (qtable[state][max] < qtable[state][integer]) {
                max = integer;
            }
            probs[integer] = 1 * this.epsilon / 4;
        }
        probs[max] += 1.0 - this.epsilon;

        EnumeratedIntegerDistribution sampler = new EnumeratedIntegerDistribution(new int[]{0, 1, 2, 3}, probs);
        return sampler.sample();
    }

    public Agent(Environnement environnement, double learning_rate, double discount_factor, double epsilon){
        this.environnement = environnement;
        this.learning_rate = learning_rate;
        this.discount_factor = discount_factor;
        this.epsilon = epsilon;
        this.qtable = new double[environnement.boardRows*environnement.boardColumns][4];
    }

    private void resetQTable(){
        for (int i = 0; i < environnement.boardRows*environnement.boardColumns; i++) {
            for (int j = 0; j < 4; j++) {
                this.qtable[i][j] = 0;
            }
        }
    }

    public double trainQ(int numberEpisodes){
        resetQTable();

        //start of time calculation for the training
        Instant start = Instant.now();

        //training phase
        for (int episode = 0; episode < numberEpisodes; episode++) {
            environnement.initState();
            int state = environnement.mouseS;
            while(true){
                int action = chooseAction(qtable, state, environnement);
                double reward = environnement.reward(action);
                int newState = environnement.state(action);
                qtable[state][action] = qtable[state][action] + learning_rate * (reward + discount_factor * qtable[newState][maxState(qtable[newState], environnement, newState)] - qtable[state][action]);
                environnement.step(action);
                state = newState;
                if(reward == environnement.bigCheeseReward || reward == environnement.poisonReward){
                    break;
                }
            }
        }
        //end of time calculation
        Instant finish = Instant.now();
        //calculate the difference between the start and the end
        return (double) Duration.between(start, finish).toMillis();
    }

    public double trainS(int numberEpisodes){
        resetQTable();
        //start of time calculation for the training
        Instant start = Instant.now();

        //train
        for (int episode = 0; episode < numberEpisodes; episode++) {
            environnement.initState();
            int state = environnement.mouseS;
            int action = chooseAction(qtable, state, environnement);
            while(true){
                double reward = environnement.reward(action);
                int newState = environnement.state(action);
                int newAction = chooseAction(qtable, newState, environnement);
                qtable[state][action] = (1 - learning_rate) * qtable[state][action] + learning_rate * (reward + discount_factor * qtable[newState][newAction]);
                environnement.step(action);
                state = newState;
                action = newAction;
                if(reward == environnement.bigCheeseReward || reward == environnement.poisonReward){
                    break;
                }
            }
        }
        //end of time calculation
        Instant finish = Instant.now();
        //calculate the difference between the start and the end
        return (double) Duration.between(start, finish).toMillis();
    }

    public double totalReward(){
        //Test phase
        environnement.initState();
        int state = environnement.mouseS;
        double ret = 0;
        double reward = 0;
        //check
        for (int step = 0; step < environnement.boardColumns * environnement.boardRows; step++) {
            int action = maxState(qtable[state], environnement, state);
            reward = environnement.reward(action);
            int newState = environnement.state(action);
            environnement.step(action);
            state = newState;

            //you may change the next line
            ret += reward;

            if(reward == environnement.bigCheeseReward || reward == environnement.poisonReward){
                break;
            }
        }
        if(reward != environnement.bigCheeseReward && reward != environnement.poisonReward){
            return -500;
        }
        return ret;
    }

    public boolean step(){
        int state = World.env.mouseS;
        double reward;
        int action = maxState(qtable[state], World.env, state);
        reward = World.env.reward(action);
        World.state = World.env.state(action);
        World.env.step(action);

        return (reward == environnement.bigCheeseReward || reward == environnement.poisonReward);
    }
}
