package com.example.calculator.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0003\u001d\u001e\u001fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0014\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014J\u0010\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0004H\u0002J\u0018\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u0004H\u0002J\u0010\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\u000b\"\u0004\b\f\u0010\r\u00a8\u0006 "}, d2 = {"Lcom/example/calculator/engine/Evaluator;", "", "()V", "BD_E", "Ljava/math/BigDecimal;", "BD_PI", "MC", "Ljava/math/MathContext;", "kotlin.jvm.PlatformType", "isDegreeMode", "", "()Z", "setDegreeMode", "(Z)V", "evalAst", "expr", "Lcom/example/calculator/engine/Evaluator$Expr;", "evaluate", "Lcom/example/calculator/engine/Evaluator$Result;", "tokens", "", "Lcom/example/calculator/engine/Token;", "factorial", "n", "pow", "base", "exp", "toRadiansOrDegrees", "v", "Expr", "Parser", "Result", "app-calculator_debug"})
public final class Evaluator {
    private static final java.math.MathContext MC = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.math.BigDecimal BD_PI = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.math.BigDecimal BD_E = null;
    private static boolean isDegreeMode = true;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calculator.engine.Evaluator INSTANCE = null;
    
    private Evaluator() {
        super();
    }
    
    public final boolean isDegreeMode() {
        return false;
    }
    
    public final void setDegreeMode(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.calculator.engine.Evaluator.Result evaluate(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.example.calculator.engine.Token> tokens) {
        return null;
    }
    
    private final java.math.BigDecimal evalAst(com.example.calculator.engine.Evaluator.Expr expr) {
        return null;
    }
    
    private final java.math.BigDecimal toRadiansOrDegrees(java.math.BigDecimal v) {
        return null;
    }
    
    private final java.math.BigDecimal factorial(java.math.BigDecimal n) {
        return null;
    }
    
    private final java.math.BigDecimal pow(java.math.BigDecimal base, java.math.BigDecimal exp) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0003\u0004\u0005\u0006\u0007B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0005\b\t\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr;", "", "()V", "BinOp", "Func", "Num", "UnaryOp", "Var", "Lcom/example/calculator/engine/Evaluator$Expr$BinOp;", "Lcom/example/calculator/engine/Evaluator$Expr$Func;", "Lcom/example/calculator/engine/Evaluator$Expr$Num;", "Lcom/example/calculator/engine/Evaluator$Expr$UnaryOp;", "Lcom/example/calculator/engine/Evaluator$Expr$Var;", "app-calculator_debug"})
    public static abstract class Expr {
        
        private Expr() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0001H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0004H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0001H\u00c6\u0003J\'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0001H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\b\u00a8\u0006\u0018"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr$BinOp;", "Lcom/example/calculator/engine/Evaluator$Expr;", "left", "op", "Lcom/example/calculator/engine/Token;", "right", "(Lcom/example/calculator/engine/Evaluator$Expr;Lcom/example/calculator/engine/Token;Lcom/example/calculator/engine/Evaluator$Expr;)V", "getLeft", "()Lcom/example/calculator/engine/Evaluator$Expr;", "getOp", "()Lcom/example/calculator/engine/Token;", "getRight", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app-calculator_debug"})
        public static final class BinOp extends com.example.calculator.engine.Evaluator.Expr {
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Evaluator.Expr left = null;
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Token op = null;
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Evaluator.Expr right = null;
            
            public BinOp(@org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr left, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Token op, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr right) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr getLeft() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Token getOp() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr getRight() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Token component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr component3() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr.BinOp copy(@org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr left, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Token op, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr right) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0005J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000b\u001a\u00020\u0001H\u00c6\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0001H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0014"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr$Func;", "Lcom/example/calculator/engine/Evaluator$Expr;", "name", "", "arg", "(Ljava/lang/String;Lcom/example/calculator/engine/Evaluator$Expr;)V", "getArg", "()Lcom/example/calculator/engine/Evaluator$Expr;", "getName", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app-calculator_debug"})
        public static final class Func extends com.example.calculator.engine.Evaluator.Expr {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String name = null;
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Evaluator.Expr arg = null;
            
            public Func(@org.jetbrains.annotations.NotNull()
            java.lang.String name, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr arg) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr getArg() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr.Func copy(@org.jetbrains.annotations.NotNull()
            java.lang.String name, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr arg) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr$Num;", "Lcom/example/calculator/engine/Evaluator$Expr;", "v", "Ljava/math/BigDecimal;", "(Ljava/math/BigDecimal;)V", "getV", "()Ljava/math/BigDecimal;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app-calculator_debug"})
        public static final class Num extends com.example.calculator.engine.Evaluator.Expr {
            @org.jetbrains.annotations.NotNull()
            private final java.math.BigDecimal v = null;
            
            public Num(@org.jetbrains.annotations.NotNull()
            java.math.BigDecimal v) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.math.BigDecimal getV() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.math.BigDecimal component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr.Num copy(@org.jetbrains.annotations.NotNull()
            java.math.BigDecimal v) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0005J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000b\u001a\u00020\u0001H\u00c6\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0001H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0015"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr$UnaryOp;", "Lcom/example/calculator/engine/Evaluator$Expr;", "op", "Lcom/example/calculator/engine/Token;", "expr", "(Lcom/example/calculator/engine/Token;Lcom/example/calculator/engine/Evaluator$Expr;)V", "getExpr", "()Lcom/example/calculator/engine/Evaluator$Expr;", "getOp", "()Lcom/example/calculator/engine/Token;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app-calculator_debug"})
        public static final class UnaryOp extends com.example.calculator.engine.Evaluator.Expr {
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Token op = null;
            @org.jetbrains.annotations.NotNull()
            private final com.example.calculator.engine.Evaluator.Expr expr = null;
            
            public UnaryOp(@org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Token op, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr expr) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Token getOp() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr getExpr() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Token component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr.UnaryOp copy(@org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Token op, @org.jetbrains.annotations.NotNull()
            com.example.calculator.engine.Evaluator.Expr expr) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Expr$Var;", "Lcom/example/calculator/engine/Evaluator$Expr;", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app-calculator_debug"})
        public static final class Var extends com.example.calculator.engine.Evaluator.Expr {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String name = null;
            
            public Var(@org.jetbrains.annotations.NotNull()
            java.lang.String name) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.calculator.engine.Evaluator.Expr.Var copy(@org.jetbrains.annotations.NotNull()
            java.lang.String name) {
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
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\u0004H\u0002J\u0006\u0010\t\u001a\u00020\nJ\b\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\nH\u0002J\b\u0010\r\u001a\u00020\nH\u0002J\b\u0010\u000e\u001a\u00020\nH\u0002J\b\u0010\u000f\u001a\u00020\u0004H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Parser;", "", "tokens", "", "Lcom/example/calculator/engine/Token;", "(Ljava/util/List;)V", "pos", "", "consume", "parseExpression", "Lcom/example/calculator/engine/Evaluator$Expr;", "parseFactor", "parsePower", "parsePrimary", "parseTerm", "peek", "app-calculator_debug"})
    static final class Parser {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.example.calculator.engine.Token> tokens = null;
        private int pos = 0;
        
        public Parser(@org.jetbrains.annotations.NotNull()
        java.util.List<? extends com.example.calculator.engine.Token> tokens) {
            super();
        }
        
        private final com.example.calculator.engine.Token peek() {
            return null;
        }
        
        private final com.example.calculator.engine.Token consume() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calculator.engine.Evaluator.Expr parseExpression() {
            return null;
        }
        
        private final com.example.calculator.engine.Evaluator.Expr parseTerm() {
            return null;
        }
        
        private final com.example.calculator.engine.Evaluator.Expr parseFactor() {
            return null;
        }
        
        private final com.example.calculator.engine.Evaluator.Expr parsePower() {
            return null;
        }
        
        private final com.example.calculator.engine.Evaluator.Expr parsePrimary() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lcom/example/calculator/engine/Evaluator$Result;", "", "value", "Ljava/math/BigDecimal;", "error", "", "(Ljava/math/BigDecimal;Ljava/lang/String;)V", "getError", "()Ljava/lang/String;", "getValue", "()Ljava/math/BigDecimal;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app-calculator_debug"})
    public static final class Result {
        @org.jetbrains.annotations.Nullable()
        private final java.math.BigDecimal value = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;
        
        public Result(@org.jetbrains.annotations.Nullable()
        java.math.BigDecimal value, @org.jetbrains.annotations.Nullable()
        java.lang.String error) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.math.BigDecimal getValue() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.math.BigDecimal component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calculator.engine.Evaluator.Result copy(@org.jetbrains.annotations.Nullable()
        java.math.BigDecimal value, @org.jetbrains.annotations.Nullable()
        java.lang.String error) {
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
}