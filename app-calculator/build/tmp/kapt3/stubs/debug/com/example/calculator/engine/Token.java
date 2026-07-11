package com.example.calculator.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u001a\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001cB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u001a\u001d\u001e\u001f !\"#$%&\'()*+,-./0123456\u00a8\u00067"}, d2 = {"Lcom/example/calculator/engine/Token;", "", "()V", "Acos", "Asin", "Atan", "Cbrt", "Cos", "Cube", "Divide", "E", "End", "Factorial", "LParen", "Ln", "Log", "Minus", "Multiply", "Negate", "Number", "Percent", "Pi", "Plus", "Power", "RParen", "Sin", "Sqrt", "Square", "Tan", "Lcom/example/calculator/engine/Token$Acos;", "Lcom/example/calculator/engine/Token$Asin;", "Lcom/example/calculator/engine/Token$Atan;", "Lcom/example/calculator/engine/Token$Cbrt;", "Lcom/example/calculator/engine/Token$Cos;", "Lcom/example/calculator/engine/Token$Cube;", "Lcom/example/calculator/engine/Token$Divide;", "Lcom/example/calculator/engine/Token$E;", "Lcom/example/calculator/engine/Token$End;", "Lcom/example/calculator/engine/Token$Factorial;", "Lcom/example/calculator/engine/Token$LParen;", "Lcom/example/calculator/engine/Token$Ln;", "Lcom/example/calculator/engine/Token$Log;", "Lcom/example/calculator/engine/Token$Minus;", "Lcom/example/calculator/engine/Token$Multiply;", "Lcom/example/calculator/engine/Token$Negate;", "Lcom/example/calculator/engine/Token$Number;", "Lcom/example/calculator/engine/Token$Percent;", "Lcom/example/calculator/engine/Token$Pi;", "Lcom/example/calculator/engine/Token$Plus;", "Lcom/example/calculator/engine/Token$Power;", "Lcom/example/calculator/engine/Token$RParen;", "Lcom/example/calculator/engine/Token$Sin;", "Lcom/example/calculator/engine/Token$Sqrt;", "Lcom/example/calculator/engine/Token$Square;", "Lcom/example/calculator/engine/Token$Tan;", "app-calculator_debug"})
public abstract class Token {
    
    private Token() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Acos;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Acos extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Acos INSTANCE = null;
        
        private Acos() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Asin;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Asin extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Asin INSTANCE = null;
        
        private Asin() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Atan;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Atan extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Atan INSTANCE = null;
        
        private Atan() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Cbrt;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Cbrt extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Cbrt INSTANCE = null;
        
        private Cbrt() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Cos;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Cos extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Cos INSTANCE = null;
        
        private Cos() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Cube;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Cube extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Cube INSTANCE = null;
        
        private Cube() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Divide;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Divide extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Divide INSTANCE = null;
        
        private Divide() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$E;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class E extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.E INSTANCE = null;
        
        private E() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$End;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class End extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.End INSTANCE = null;
        
        private End() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Factorial;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Factorial extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Factorial INSTANCE = null;
        
        private Factorial() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$LParen;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class LParen extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.LParen INSTANCE = null;
        
        private LParen() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Ln;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Ln extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Ln INSTANCE = null;
        
        private Ln() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Log;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Log extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Log INSTANCE = null;
        
        private Log() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Minus;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Minus extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Minus INSTANCE = null;
        
        private Minus() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Multiply;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Multiply extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Multiply INSTANCE = null;
        
        private Multiply() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Negate;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Negate extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Negate INSTANCE = null;
        
        private Negate() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/example/calculator/engine/Token$Number;", "Lcom/example/calculator/engine/Token;", "value", "Ljava/math/BigDecimal;", "(Ljava/math/BigDecimal;)V", "getValue", "()Ljava/math/BigDecimal;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app-calculator_debug"})
    public static final class Number extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        private final java.math.BigDecimal value = null;
        
        public Number(@org.jetbrains.annotations.NotNull()
        java.math.BigDecimal value) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.math.BigDecimal getValue() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.math.BigDecimal component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calculator.engine.Token.Number copy(@org.jetbrains.annotations.NotNull()
        java.math.BigDecimal value) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Percent;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Percent extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Percent INSTANCE = null;
        
        private Percent() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Pi;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Pi extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Pi INSTANCE = null;
        
        private Pi() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Plus;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Plus extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Plus INSTANCE = null;
        
        private Plus() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Power;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Power extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Power INSTANCE = null;
        
        private Power() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$RParen;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class RParen extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.RParen INSTANCE = null;
        
        private RParen() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Sin;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Sin extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Sin INSTANCE = null;
        
        private Sin() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Sqrt;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Sqrt extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Sqrt INSTANCE = null;
        
        private Sqrt() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Square;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Square extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Square INSTANCE = null;
        
        private Square() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/calculator/engine/Token$Tan;", "Lcom/example/calculator/engine/Token;", "()V", "app-calculator_debug"})
    public static final class Tan extends com.example.calculator.engine.Token {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.calculator.engine.Token.Tan INSTANCE = null;
        
        private Tan() {
        }
    }
}