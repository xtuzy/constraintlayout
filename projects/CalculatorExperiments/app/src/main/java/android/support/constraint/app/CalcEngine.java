package android.support.constraint.app;

import android.util.Log;

import androidx.constraintlayout.motion.widget.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class CalcEngine {
    private static final String TAG = "CalcEngin";
    private static final boolean DEBUG = false;
    HashMap<String, Op> mOperators = new HashMap<>();
    Stack stack = new Stack();

    private void add(Op op) {
        mOperators.put(op.key, op);
    }

    public CharSequence getStack(int i) {
        return stack.getString(i);
    }

    public static class Symbolic {
        double value = Double.NaN;
        String var = null;
        Op operator;
        Symbolic lhs, rhs;

        int dimensions() {
            if ("x".equals(var)) {
                return 1;
            }
            if ("y".equals( var)) {
                return 2;
            }
            if (!Double.isNaN(value)) {
                return 0;
            }
            return lhs.dimensions() | ((rhs==null)?0: rhs.dimensions());
        }
        public Symbolic(String val) {
            var = val;
        }

        public Symbolic(Op op, Symbolic lhs, Symbolic rhs) {
            operator = op;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public Symbolic(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (operator != null) {
                if (operator instanceof Op2) {
                    if (rhs == null) {
                        return "(" + operator.key + " error)";
                    }
                    if (lhs != null && rhs !=null) {
                        String left = (lhs.operator instanceof Op2) ? "(" + lhs + ")" : lhs.toString();
                        String right = (rhs.operator instanceof Op2) ? "(" + rhs + ")" : rhs.toString();
                        return left + " " + operator + " " + right;
                    }
                }
                return operator + "(" + lhs + ")";
            }
            return (var != null) ? var : Double.toString(value);
        }

        public double eval(double x, double y, Stack stack) {
            if (var != null) {  // a variable x or y
                if ("x".equals(var)) {
                    return x;
                } else if ("y".equals(var)) {
                    return y;
                }
            }
            if (Double.isNaN(value)) { // it is not a hard value
                stack.push(lhs.eval(x, y, stack));
                if (operator instanceof Op2) {
                    stack.push(rhs.eval(x, y, stack));
                }
                operator.op(stack);
                return stack.pop();
            }
            return value;
        }
    }

    public static class Stack {
        double[] values = new double[5];
        Symbolic[] sval = new Symbolic[5];
        int top = 0;

        void dump() {
            Log.v(TAG, Debug.getLoc() + " ================= ");
            for (int i = 0; i < top; i++) {
                Log.v(TAG, Debug.getLoc() + " [" + i + "]" + getString(i));
            }
            Log.v(TAG, Debug.getLoc() + " ================= ");
        }

        void push(String val) {
            resize();
            sval[top++] = new Symbolic(val);
        }

        private void resize() {
            if (top > values.length - 1) {
                values = Arrays.copyOf(values, values.length * 2);
                sval = Arrays.copyOf(sval, values.length * 2);
            }
        }
        void push(double val) {
            resize();
            values[top] = val;
            sval[top] = null;
            top++;
        }

        void pop(int d) {
            top -= d;
        }

        double pop() {
            if (top == 0) {
                return Float.NaN;
            }
            return values[--top];
        }

        double peek() {
            if (top == 0) {
                return Float.NaN;
            }
            return values[top - 1];
        }

        void clear() {
            top = 0;
        }

        void swap() {
            if (top < 2) {
                return;
            }
            double tmp = values[top - 1];
            values[top - 1] = values[top - 2];
            values[top - 2] = tmp;
            Symbolic stemp = sval[top - 1];
            sval[top - 1] = sval[top - 2];
            sval[top - 2] = stemp;
        }

        void dup() {
            resize();
            if (top < 1) {
                return;
            }
            sval[top] = sval[top - 1];
            values[top] = values[top - 1];
            top++;
        }

        double pop1() {
            swap();
            return pop();
        }

        private int stackIndex(int i) {
            return top - 1 - i;
        }

        public boolean isVar(int i) {
            if (i + 1 > top) return false;
            return sval[top - 1 - i] != null;
        }

        public Symbolic getVar(int i) {
            if (i + 1 > top) return null;
            int si = stackIndex(i);
            if (sval[si] != null) {
                return sval[si];
            }
            return new Symbolic(values[si]);
        }

        public void push(Symbolic symbolic) {
            resize();
            sval[top++] = symbolic;
        }

        public CharSequence getString(int i) {
            if (i + 1 > top) return "";
            if (isVar(i)) {
                return getVar(i).toString();
            }
            return Float.toString((float) values[stackIndex(i)]);
        }
    }

    abstract class Op {
        final String key;
        int args = 1;

        @Override
        public String toString() {
            return key;
        }

        protected Op(String key) {
            this.key = key;
            add(this);
        }

        abstract void op(Stack s);
    }

    abstract class Op2 extends Op {


        protected Op2(String key) {
            super(key);
            args = 2;
        }

        abstract void op(Stack s);
    }


    Op factorial = new Op("!") {

        double gamma(double x) {
            double ret = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
            double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
                    + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
                    + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
            ret = ret + Math.log(ser * Math.sqrt(2 * Math.PI));
            return Math.exp(ret);
        }

        void op(Stack s) {
            double v = (int) s.pop();
            if (true) {
                s.push(gamma(v));
                return;
            }
            int ival = (int) v;
            for (int i = 1; i < ival; i++) {
                v *= i;
            }
            s.push(v);
        }
    };

    // @formatter:off
    Op add = new Op2("+") { void op(Stack s) {
    s.push(s.pop() + s.pop());
} };
    Op sub = new Op2("-") { void op(Stack s) { s.push(s.pop() - s.pop()); } };
    Op mul = new Op2("✕") { void op(Stack s) { s.push(s.pop() * s.pop()); }    };
    Op div = new Op2("÷") { void op(Stack s) { s.push(s.pop1() / s.pop()); } };
    Op pow = new Op2("^") { void op(Stack s) { s.push(Math.pow(s.pop1() , s.pop())); } };

    Op sgn = new Op("+/-") {  void op(Stack s) { s.push(-s.pop());  } };
    Op sin = new Op("sin") {  void op(Stack s) { s.push(Math.sin(s.pop()));  } };
    Op cos = new Op("cos") {  void op(Stack s) { s.push(Math.cos(s.pop()));  } };
    Op tan = new Op("tan") {  void op(Stack s) { s.push(Math.tan(s.pop()));  } };
    Op exp = new Op("exp") {  void op(Stack s) { s.push(Math.exp(s.pop()));  } };
    Op sin1 = new Op("sin-1") {  void op(Stack s) { s.push(Math.asin(s.pop()));  } };
    Op cos1 = new Op("cos-1") {  void op(Stack s) { s.push(Math.acos(s.pop()));  } };
    Op tan1 = new Op("tan-1") {  void op(Stack s) { s.push(Math.atan(s.pop()));  } };
    Op log = new Op("log") {  void op(Stack s) { s.push(Math.log10(s.pop()));  } };
    Op drop = new Op("drop") {  void op(Stack s) { s.pop();  } };

    Op clr = new Op("ac") {  void op(Stack s) {   s.clear();  } };
    Op ln = new Op("ln") {  void op(Stack s) {   s.push(Math.log(s.pop()));  } };
    Op sq = new Op("sq") {  void op(Stack s) {   s.push((s.peek() * s.pop()));  } };
    Op inv = new Op("inv") {  void op(Stack s) {   s.push(1 / (s.pop()));  } };
    Op root = new Op("√") {  void op(Stack s) {   s.push(Math.sqrt(s.pop()));  } };
    Op pi = new Op("Π") {  void op(Stack s) {   s.push(Math.PI);  } };
    Op e = new Op("e") {  void op(Stack s) {   } };
    Op swap = new Op("swap") {  void op(Stack s) {   s.swap();  } };

    Op dup = new Op("dup") {  void op(Stack s) {   s.dup();  } };

// @formatter:on

    HashSet<String> stackOperation = new HashSet<>(Arrays.asList("dup", "swap", "drop"));
    HashSet<String> flushKeys = new HashSet<>(Arrays.asList("x", "y", "enter", "e", "Π"));

    double screen = 0;
    int period = 0;
    boolean numberSet = true;
    String entryString = "";

    String key(String str) {
        if (numberSet) {
            screen = 0;
            period = 0;
        } else if (flushKeys.contains(str)) {
            stack.push(Double.parseDouble(entryString));
            entryString = "";
            numberSet = true;
        }

        int v = -1;
        switch (str) {
            case "x":
            case "y":
                stack.push(str);
                break;
            case "Π":
                stack.push(Math.PI);
                break;
            case "enter":
                break;
            case "+/-":
                if (numberSet) {
                    eval(str);
                } else {
                    entryString = (entryString.startsWith("-")) ? entryString.substring(1) : ("-" + entryString);
                }
                break;
            case "-":
                if (numberSet) {
                    eval(str);
                } else {
                    if (entryString.endsWith("E")) {
                        entryString = "-";
                    } else {
                        eval(str);
                    }
                }
                break;
            case "eex":
                entryString += "E";
                break;
            case ".":
            case "9":
            case "8":
            case "7":
            case "6":
            case "5":
            case "4":
            case "3":
            case "2":
            case "1":
            case "0":
                entryString += str;
                numberSet = false;
                break;
            default:
                if (!numberSet) {
                    stack.push(Double.parseDouble(entryString));
                    entryString = "";
                }
                eval(str);
                numberSet = true;
        }

       if (DEBUG) {
           stack.dump();
           Log.v(TAG, " entryString = \"" + entryString + "\"");
       }
        return entryString;
    }

    void eval(String op) {
        Op o = mOperators.get(op);
        if (stackOperation.contains(op)) {
            o.op(stack);
            return;
        }
        if (o instanceof Op2) {
            if (stack.isVar(0) || stack.isVar(1)) {
                Symbolic v1 = stack.getVar(0);
                Symbolic v2 = stack.getVar(1);
                stack.pop();
                stack.pop();
                stack.push(new Symbolic(o, v2, v1));
                return;
            }
        } else {
            if (stack.isVar(0)) {
                Symbolic v1 = stack.getVar(0);
                stack.pop();
                stack.push(new Symbolic(o, v1, null));
                return;
            }
        }
        if (o == null) {
            Log.v(TAG, "NO operator for \"" + op + "\"");
            return;
        }
        o.op(stack);
    }
}
