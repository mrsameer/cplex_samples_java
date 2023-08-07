package org.sameer;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class LinearEquationExample1 {
    public static void main(String[] args) {
        IloCplex cplex = null;
        try {
            // Create the modeler/solver object
            cplex = new IloCplex();


            // Create variables
            IloNumVar x = cplex.numVar(0.0, Double.MAX_VALUE, "x");
            IloNumVar y = cplex.numVar(0.0, Double.MAX_VALUE, "y");

            // Add objective function: Maximize 3x + 4y
            IloLinearNumExpr objective = cplex.linearNumExpr();
            objective.addTerm(3, x);
            objective.addTerm(4, y);
            cplex.addMaximize(objective);

            // Add constraints
            cplex.addLe(cplex.sum(x, y), 5.0); // x + y <= 5
            cplex.addLe(cplex.sum(cplex.prod(2, x), y), 8.0); // 2x + y <= 8

            // Solve the model
            if (cplex.solve()) {
                System.out.println("Objective value = " + cplex.getObjValue());
                System.out.println("x = " + cplex.getValue(x));
                System.out.println("y = " + cplex.getValue(y));
            } else {
                System.out.println("No solution found");
            }
        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
        } finally {
            // Clean up
            if (cplex != null) {
                cplex.end();
            }
        }
    }
}
