package nicod;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import api.Expression;
import api.Inference;
import api.Line;
import api.Proof;
import api.exception.InvalidExpressionException;
import api.implementation.LineImpl;

import util.Triple;

/**
 * @author Ethan
 *
 */
public class NicodProof implements Proof {
	private static final long serialVersionUID = -2104100764291815105L;

	private class NullOutputStream extends OutputStream {
	    public void write(int i) throws IOException {
	        //do nothing
	    }
	}
	
	Map<Expression, Proof> proven;
	List<Inference> rules;
	List<Expression> premise;
	List<Line> proof;

	public NicodProof() {
		proven = new HashMap<Expression, Proof>();
		rules = new LinkedList<Inference>();
		premise = new ArrayList<Expression>();
	}

	public NicodProof(Collection<Inference> rules) {
		proven = new HashMap<Expression, Proof>();
		this.rules = new LinkedList<Inference>(rules);
		premise = new ArrayList<Expression>();
		
		for(Inference r : rules) {
			this.rules.add((Inference) r.deepClone());
		}
	}

	public NicodProof(Collection<Inference> rules, Collection<Expression> premise) {
		proven = new HashMap<Expression, Proof>();
		this.rules = new LinkedList<Inference>();
		this.premise = new ArrayList<Expression>();

		for(Inference r : rules) {
			this.rules.add((Inference) r.deepClone());
		}
		
		for(Expression e : premise) {
			this.premise.add(e.normalize());
		}
	}

	public boolean addRule(Inference rule) {
		return rules.add(rule);
	}

	public boolean removeRule(Inference rule) {
		return rules.remove(rule);
	}

	public boolean addPremise(Expression premise) throws InvalidExpressionException {
		if(premise == null || !premise.isValid()) throw new InvalidExpressionException();
		
		return this.premise.add(premise);
	}

	public boolean removePremise(Expression premise) {
		return this.premise.remove(premise.normalize());
	}

	@Override
	public List<? extends Expression> prove(Expression goal) throws InvalidExpressionException {
		return prove(goal, new BufferedWriter(new OutputStreamWriter(System.out)));
	}
	
	
	public List<? extends Expression> prove(Expression goal, boolean verbose) throws InvalidExpressionException {
		List<? extends Expression> ret;
		if(verbose) {
			ret = prove(goal, new BufferedWriter(new OutputStreamWriter(System.out)));
		}
		else {
		
			ret = prove(goal, new BufferedWriter(new OutputStreamWriter(new NullOutputStream())));
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see Proof#prove(java.util.List, Expression)
	 */
	@Override
	public List<? extends Expression> prove(Expression goal, BufferedWriter writer) throws InvalidExpressionException {
		long start = System.nanoTime();
		//TODO
		try {
			writer.write("Starting Proof Generation");
			writer.newLine();
			writer.flush();
		} catch (IOException e3) {
		}
		
		//Make a list of Lines in the proof
		proof = new ArrayList<Line>();
		
		//Make a list of the queues for different inference rules
		List<Queue<Triple<Inference, Line, Line>>> queues = new LinkedList<Queue<Triple<Inference, Line, Line>>>();
		for(int i = 0; i < rules.size(); i++) {
			queues.add(new ConcurrentLinkedQueue<Triple<Inference, Line, Line>>());
		}
		//Queue<Triple<Inference, Line, Line>> queue = new ConcurrentLinkedQueue<Triple<Inference, Line, Line>>();
		//Queue<Triple<Inference, Line, Line>> queueMP = new ConcurrentLinkedQueue<Triple<Inference, Line, Line>>();
		
		/*Queue<Triple<Inference, Line, Line>> queue = new PriorityQueue<Triple<Inference, Line, Line>>(110, new Comparator<Triple<Inference, Line, Line>>(){
			@Override
			public int compare(Triple<Inference, Line, Line> arg0, Triple<Inference, Line, Line> arg1) {
				return arg0.getSecond(). new LineUseComparator().compare(arg0.getSecond(), arg1.getSecond()) - 
						arg0.getSecond(). new LineUseComparator().compare(arg0.getThird(), arg1.getThird());
				//return 0;
				//return arg0.getFirst().getPriority() - arg1.getFirst().getPriority();
			}
		});*/
		
		//Add each premise to the proof
		for(Expression e : premise) {
			Line newLine = new LineImpl(e, proof.size() + 1);
			try {
				writer.write(newLine.toString());
				writer.newLine();
				writer.flush();
			} catch (IOException e1) {
			}
			proof.add(newLine);
			
			//try {
			//	Main.logbf.write(newLine + "\n");
			//	Main.logbf.flush();
			//} catch (IOException e2) {
			//}
			
			
			//If a premise is the goal we are done
			if(e.equals(goal)) {
				proven.put(goal, this.deepClone());
				return proof;
			}
		}
		
		for(int i = 0; i < rules.size(); i++) {
			for(Line e1 : proof) {
				for(Line e2 : proof) {
					//if(r instanceof NicodMP) queueMP.add(new Triple<Inference, Line, Line>(r.deepClone(), e1.deepClone(), e2.deepClone()));
					//else queue.add(new Triple<Inference, Line, Line>(r.deepClone(), e1.deepClone(), e2.deepClone()));
					
					//For each rule and each pair of lines in the proof, add them to their respective queue
					queues.get(i).add(new Triple<Inference, Line, Line>(rules.get(i), e1, e2));
				}
			}
		}
		Line newLine;
		boolean done = false;
		while(!done) {
			Triple<Inference, Line, Line> cur = null;
			//done = true;
			
			for(Queue<Triple<Inference, Line, Line>> q : queues) {
				if(!q.isEmpty()) {
					cur = q.poll();
					break;
				}
			}
			if(cur == null) break;
			//if(!queueMP.isEmpty()) cur = queueMP.poll();
			//else cur = queue.poll();
			
			Expression newExpression = cur.getFirst().infer(cur.getSecond(), cur.getThird());
			if(newExpression != null) {
				//newLine = new Line(newExpression, cur.getFirst().toString().replaceAll("<1>",cur.getSecond().lineNum+"").replaceAll("<2>",cur.getThird().lineNum+""), proof.size()+1);
				newLine = new LineImpl(newExpression, cur.getFirst(), cur.t, proof.size()+1);
				if(newLine != null) {
					newLine.normalize();
				
					for(Line l : proof) {
						if(l.equals(newLine)) {
							newLine = null;
							break;
						}
					}
					if(newLine != null) {
						try {
							writer.write(newLine.toString());
							writer.newLine();
							long duration = System.nanoTime() - start;
							int days = (int) TimeUnit.NANOSECONDS.toDays(duration);
							duration -= TimeUnit.DAYS.toNanos(days);
							int hours = (int) (TimeUnit.NANOSECONDS.toHours(duration));
							duration -= TimeUnit.HOURS.toNanos(hours);
							int minutes = (int) (TimeUnit.NANOSECONDS.toMinutes(duration));
							duration -= TimeUnit.MINUTES.toNanos(minutes);
							int seconds = (int) (TimeUnit.NANOSECONDS.toSeconds(duration));
							duration -= TimeUnit.SECONDS.toNanos(seconds);
							long milli = (int) (TimeUnit.NANOSECONDS.toMillis(duration));
							writer.write("Time taken so far:\t" + String.format("%dd%02d:%02d:%02d.%03d", days, hours, minutes, seconds, milli));
							writer.newLine();
							writer.flush();
						} catch (IOException e2) {
						}
						proof.add(newLine);
						//try {
						//	Main.logbf.write(newLine + "\n");
						//	Main.logbf.flush();
						//} catch (IOException e2) {
						//}
						if(newLine.equals(goal)) {
							proven.put(goal, this.deepClone());
							long end = System.nanoTime();
							try {
								writer.write("Seconds Taken: " + (end - start)/Math.pow(10, 9));
								writer.newLine();
								writer.flush();
							} catch (IOException e2) {
							}
							return proof;
						}
						for(int i = 0; i < rules.size(); i++) {
							for(Line e1 : proof) {
								queues.get(i).add(new Triple<Inference, Line, Line>(rules.get(i), newLine, e1));
								queues.get(i).add(new Triple<Inference, Line, Line>(rules.get(i), e1, newLine));
								//if(r instanceof NicodMP) queueMP.add(new Triple<Inference, Line, Line>(r.deepClone(), e1.deepClone(), newLine.deepClone()));
								//else queue.add(new Triple<Inference, Line, Line>(r.deepClone(), e1.deepClone(), newLine.deepClone()));
								
								//if(r instanceof NicodMP) queueMP.add(new Triple<Inference, Line, Line>(r.deepClone(), newLine.deepClone(), e1.deepClone()));
								//else queue.add(new Triple<Inference, Line, Line>(r.deepClone(), newLine.deepClone(), e1.deepClone()));
								
								
							}
						}
					}
				}
			}
		}
		proven.put(goal, null);
		long end = System.nanoTime();
		try {
			writer.write("Seconds Taken: " + (end - start)/Math.pow(10, 9));
			writer.newLine();
			writer.flush();
		} catch (IOException e2) {
		}
		return proof;
		/*proof = new ArrayList<Line>();
		//SortedSet<Line> newProof = new ConcurrentSkipListSet<Line>();
		for(Expression e : premise) {
			proof.add(new Line(e, "Premise", proof.size() + 1));
		}
		for(Line l : proof) {
			if(l.equals(goal)) {
				proven.put(goal, true);
				//proof.addAll(newProof);
				return proof;
			}
		}
		//if(newProof.contains(goal)) {
		//	proven.put(goal, true);
		//	proof.addAll(newProof);
		//	return proof;
		//}
		Queue<Line> queue = new PriorityQueue<Line>(11, new Line().new LineUseComparator());
		Queue<Line> queue2 = new PriorityQueue<Line>(11, new Line().new LineUseComparator());
		//queue.addAll(proof);
		//queue2.addAll(proof);
		boolean gotGoal = false;
		boolean done = false;
		int count = 15;
		while(!done && count > 1) {
			count--;
			System.out.println(count);
			//for(int i = proof.size() - 1; i >= 0; i--) {
			//	//queue.addAll(proof);
			//	//queue2.addAll(proof);
			//	queue.add(proof.get(i));
			//	queue2.add(proof.get(i));
			//}
			//proof.addAll(newProof);
			//System.out.println(this);
			//proof = new ArrayList<Line>();
			done = true;
			Line newLine = null;
			Line current = null;
			Line e = null;
			queue.clear();
			for(Inference rule : rules) {
				if(newLine != null) break;
				for(int i = proof.size() - 1; i >= 0; i--) queue.add(proof.get(i));
				//for(int i = 0; i < proof.size(); i++) queue.add(proof.get(i));
				//System.out.println(queue);

				while(!queue.isEmpty()) {
					current = queue.poll();
					//System.out.println("queue = " + queue);
					if(newLine != null) break;
					//queue2.addAll(queue);
					queue2.clear();
					for(int i = proof.size() - 1; i >= 0; i--) queue2.add(proof.get(i));
					//for(int i = 0; i < proof.size(); i++) queue2.add(proof.get(i));
					//System.out.println(queue2);
					while(!queue2.isEmpty()) {
						e = queue2.poll();
						//System.out.println("\ncurrent = " + current);
						//System.out.println("e = " + e);
						if(newLine != null) break;

						//System.out.println("\nrule = " + rule);
						//System.out.println("current = " + current);
						//System.out.println("e = " + e);
						Expression newExpression = rule.infer(current, e);
						if(newExpression != null) {
							newLine = new Line(newExpression, rule.toString().replaceAll("<1>",current.lineNum+"").replaceAll("<2>",e.lineNum+""), proof.size()+1);
							newLine.normalize();
						}
						if(newLine != null) {
							//boolean isNew = true;
							if(newLine.equals(goal)) gotGoal = true;
							for(Line l : proof) {
								if(l.equals(newLine)) {
									//isNew = false;
									newLine = null;
									break;
								}
							}
							current.use();
							if(!current.equals(e)) e.use();
							//System.out.println(this);

							//if(isNew) {
							//	System.out.println(newLine);
							//	proof.add(newLine);
							//	done = false;
							//	if(newLine.equals(goal)) gotGoal = true;
							//}

						}
					}
				}
			}
			if(newLine != null) {
				queue.add(newLine);
				proof.add(newLine);
				done = false;
				//System.out.println(this);
			}
			if(gotGoal) {
				proven.put(goal, true);
				//proof.addAll(newProof);
				return proof;
			}
		}
		proven.put(goal, false);
		//proof.addAll(newProof);
		//System.out.println(this);
		return proof;*/
	}

	public String toString() {
		String result = "";
		for(Line e : proof) {
			result += e + "\n";
		}
		return result.trim();
	}
	
	public NicodProof deepClone() {
		NicodProof clone = new NicodProof();
		//PriorityQueue<Inference> rules;
		for(Inference i : rules) {
			clone.addRule((Inference) i.deepClone());
		}
		
		//List<Expression> premise;
		for(Expression e : premise) {
			try {
				clone.addPremise((Expression) e.deepClone());
			} catch (InvalidExpressionException e1) {
				e1.printStackTrace();
			}
		}
		
		//List<Line> proof;
		for(Line l : proof) {
			if(clone.proof != null) clone.proof.add(l.deepClone());
		}
		
		//Map<Expression, Proof> proven;
		for(Expression e : proven.keySet()) {
			clone.proven.put(e, proven.get(e));
		}
		
		return clone;
	}

	@Override
	public Proof clone() {
		NicodProof clone = new NicodProof();
		clone.rules = rules;
		clone.premise = premise;
		clone.proof = proof;
		clone.proven = proven;
		return clone;
	}

	@Override
	public Line getLine(int n) {
		return proof.get(n);
	}
	
	@Override
	public int length() {
		return proof.size();
	}
}
