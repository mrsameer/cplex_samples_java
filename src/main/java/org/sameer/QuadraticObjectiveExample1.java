package org.sameer;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloQuadNumExpr;
import ilog.cplex.IloCplex;


public class QuadraticObjectiveExample1 {
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

            // Create quadratic objective expression: Maximize 2x^2 + 3y^2 + 4xy
            IloNumExpr quadraticObjective = cplex.sum(
                    cplex.prod(2.0, x, x), // 2x^2
                    cplex.prod(3.0, y, y), // 3y^2
                    cplex.prod(4.0, x, y)  // 4xy
            );

            cplex.addMaximize(quadraticObjective);


            // Add constraint: x + y <= 5
            cplex.addLe(cplex.sum(x, y), 5.0);

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
