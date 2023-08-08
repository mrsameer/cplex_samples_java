package org.sameer;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;


public class QuadraticObjectiveExample3 {
    public static void main(String[] args) {
        IloCplex cplex = null;

        try {
            // Create the modeler/solver object
            cplex = new IloCplex();

            // Searches for a globally optimal solution to a nonconvex model; changes problem type to MIQP if necessary.
            cplex.setParam(IloCplex.Param.OptimalityTarget, 3);

            // Create variables
            IloNumVar x = cplex.numVar(0.0, Double.MAX_VALUE, "x");
            IloNumVar y = cplex.numVar(0.0, Double.MAX_VALUE, "y");

            // Create quadratic objective expression: (x - 2)^2 + (y + 3)^2
            IloNumExpr xMinus2 = cplex.diff(x, 2.0); // x - 2
            IloNumExpr yPlus3 = cplex.sum(y, 3.0);   // y + 3

            IloNumExpr quadraticObjective = cplex.sum(
                    cplex.prod(xMinus2, xMinus2), // (x - 2)^2
                    cplex.prod(yPlus3, yPlus3)     // (y + 3)^2
            );

            cplex.addMaximize(quadraticObjective); // Maximize the quadratic objective

            // Add constraints
            cplex.addGe(x, 0.0);            // x >= 0
            cplex.addGe(y, 0.0);            // y >= 0
            cplex.addLe(cplex.sum(x, y), 10.0); // x + y <= 10

            // Solve the problem
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
