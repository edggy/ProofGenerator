import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import nicod.*;
import api.*;
import api.exception.*;


public class Main {

	public static BufferedWriter logbf = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String prove = "|X1||X2|X1X1|X2|X1X1";
		Notation not = Notation.Prefix;
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-o")) {
				try {
					logbf = new BufferedWriter(new FileWriter(args[i+1], true));
					logbf.newLine();
				} catch (IOException e1) {
				}
			}
			else if(args[i].equals("-p")) {
				prove = args[i+1];
			}
			else if(args[i].equals("-n")) {
				if(args[i+1].equalsIgnoreCase("prefix")) {
					not = Notation.Prefix;
				}
				else if(args[i+1].equalsIgnoreCase("infix")) {
					not = Notation.Infix;
				}
				else if(args[i+1].equalsIgnoreCase("postfix")) {
					not = Notation.Postfix;
				}
			}
		}
		Proof p = new NicodProof();
		Inference mpInference = new NicodMP();
		Inference subInference = new NicodSub();

		/*Variable phi = new VariableImpl();
		Expression notphi = null;
		Expression psi = new NicodExpression();
		Expression tau = new NicodExpression();
		Expression x = null;
		Expression left = null;
		Expression right = null;
		Expression premise = null;
		Expression goal = null;
		Expression rightP = null;
		List<Expression> step = new ArrayList<Expression>();
		Expression step2 = null;
		Expression step2mp = null;
		Expression step3 = null;
		Expression step3mp = null;
		Expression step4 = null;
		Expression tmp = null;
		Expression simp = null;
		try {
			x = new NicodExpression("(A|(B|C))");
			left = new NicodExpression("(A|(A|A))");
			right = new NicodExpression("((A|A)|A)");
			notphi = new NicodExpression(new Nand(),phi,phi);
			premise = new NicodExpression("((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))");
			rightP = new NicodExpression("((D|B)|((A|D)|(A|D)))");
			goal = new NicodExpression("((((D|B)|((A|D)|(A|D)))|(D|(D|D)))|(((A|(A|A))|((D|B)|((A|D)|(A|D))))|((A|(A|A))|((D|B)|((A|D)|(A|D))))))");
			step.add(premise);
			
			step2 = new NicodExpression("(((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))|((E|(E|E))|((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))))").normalize();
			step2mp = new NicodExpression("((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))").normalize();
			step3 = new NicodExpression("(((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))|((F|(F|F))|((F|((A|(B|C))|E))|(((E|(D|(D|D)))|F)|(((E|(D|(D|D)))|F))))))").normalize();
			step3mp = new NicodExpression("((F|((A|(B|C))|E))|(((E|(D|(D|D)))|F)|(((E|(D|(D|D)))|F))))").normalize();
			step4 = new NicodExpression("(((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))|(((((D|B)|((A|D)|(A|D)))|(E|(E|E)))|(A|(B|C)))|(((((D|B)|((A|D)|(A|D)))|(E|(E|E)))|(A|(B|C))))))").normalize();
			step.add(step2);
			step.add(step2mp);
			step.add(step3);
			step.add(step3mp);
			step.add(step4);
			step.add(goal);
			simp = new NicodExpression("(A|((B|(A|A))|(B|(A|A))))").normalize();
			
			
			p.addPremise(premise);
			//p.addPremise(left);
			p.addRule(mpInference);
			p.addRule(subInference);
			
			p.prove(simp, logbf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println(p);
		for(int i = 0; i < p.length(); i++) {
			System.out.println(p.getLine(i).isTautology() + "\t" + p.getLine(i));
		}*/
		try {
			Expression x = new NicodTreeExpression("|X1|X1X1", Notation.Prefix);
			//System.out.println(x.infix());
			
			Expression premisePre = new NicodTreeExpression("||X1|X2X3||X4|X4X4||X4X2||X1X4|X1X4", Notation.Prefix);
			//Expression premiseIn = new NicodTreeExpression("((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))", Notation.Infix);
			Expression simpPre = new NicodTreeExpression(prove, not);
			//System.out.println(premise.infix());
			//System.out.println("premisePre = " + premisePre.infix());
			//System.out.println("premisePre = " + premisePre.isTautology());
			//System.out.println("premiseIn = " + premiseIn.infix());
			p.addPremise(premisePre);
			p.addRule(mpInference);
			p.addRule(subInference);
			
			if(logbf != null) p.prove(simpPre, logbf);
			else p.prove(simpPre);
			//p.prove(simpPre,false);
		} catch (InvalidExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
