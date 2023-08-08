package org.sameer;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;


public class QuadraticObjectiveExample2 {
    public static void main(String[] args) {
        IloCplex cplex = null;

        try {
            // Create the modeler/solver object
            cplex = new IloCplex();

            // Searches for a globally optimal solution to a nonconvex model; changes problem type to MIQP if necessary.
            cplex.setParam(IloCplex.Param.OptimalityTarget, 3);

            // Create variables
            IloNumVar x = cplex.numVar(0.0, Double.MAX_VALUE, "x");

            // Create quadratic objective expression: Maximize 3x^2 - 2x + 1
            IloNumExpr quadraticObjective = cplex.sum(
                    cplex.prod(3.0, x, x), // 3x^2
                    cplex.prod(-2.0, x)    // -2x
            );
            quadraticObjective = cplex.sum(quadraticObjective, cplex.constant(1.0)); // Add constant term

            cplex.addMaximize(quadraticObjective);


            // Add constraints: x >= 0 and x <= 5
            cplex.addGe(x, 0.0); // x >= 0
            cplex.addLe(x, 5.0); // x <= 5

            // Solve the problem
            if (cplex.solve()) {
                System.out.println("Objective value = " + cplex.getObjValue());
                System.out.println("x = " + cplex.getValue(x));
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
