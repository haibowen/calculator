package com.example.calculator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001c\u001a\u00020\u001dJ\b\u0010\u001e\u001a\u00020\u001dH\u0002J\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u0018J\u0010\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u0007H\u0002J\u0006\u0010%\u001a\u00020\u001dJ\u0006\u0010&\u001a\u00020\u001dJ\u000e\u0010\'\u001a\u00020\u001d2\u0006\u0010(\u001a\u00020\u0007J\u0006\u0010)\u001a\u00020\u001dJ\u000e\u0010*\u001a\u00020\u001d2\u0006\u0010+\u001a\u00020\u0007J\u0006\u0010,\u001a\u00020\u001dJ\u0006\u0010-\u001a\u00020\u001dJ\u000e\u0010.\u001a\u00020\u001d2\u0006\u0010(\u001a\u00020\u0007J\u000e\u0010/\u001a\u00020\u001d2\u0006\u00100\u001a\u00020\u0007J\u0006\u00101\u001a\u00020\u001dJ\u0006\u00102\u001a\u00020\u001dJ\u0006\u00103\u001a\u00020\u001dJ\u0006\u00104\u001a\u00020\u001dJ\b\u00105\u001a\u00020\u001dH\u0002R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u00070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u00070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00170\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013\u00a8\u00066"}, d2 = {"Lcom/example/calculator/CalculatorViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_errorText", "Landroidx/lifecycle/MutableLiveData;", "", "_expressionText", "kotlin.jvm.PlatformType", "_resultText", "currentInput", "dao", "Lcom/example/calculator/model/HistoryDao;", "displayOperators", "", "errorText", "Landroidx/lifecycle/LiveData;", "getErrorText", "()Landroidx/lifecycle/LiveData;", "expressionText", "getExpressionText", "history", "", "Lcom/example/calculator/model/HistoryEntry;", "getHistory", "resultText", "getResultText", "clearHistory", "", "evaluateCurrent", "isDegreeMode", "", "loadFromHistory", "entry", "normalizeExpression", "input", "onBackspace", "onClear", "onConstant", "name", "onDecimal", "onDigit", "digit", "onEquals", "onFactorial", "onFunction", "onOperator", "op", "onParentheses", "onPercent", "onPower", "toggleAngleMode", "updateDisplay", "app-calculator_debug"})
public final class CalculatorViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.calculator.model.HistoryDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _expressionText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> expressionText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _resultText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> resultText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _errorText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> errorText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.calculator.model.HistoryEntry>> history = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentInput = "";
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.String> displayOperators = null;
    
    public CalculatorViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getExpressionText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getResultText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getErrorText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.calculator.model.HistoryEntry>> getHistory() {
        return null;
    }
    
    public final void onDigit(@org.jetbrains.annotations.NotNull()
    java.lang.String digit) {
    }
    
    public final void onDecimal() {
    }
    
    public final void onOperator(@org.jetbrains.annotations.NotNull()
    java.lang.String op) {
    }
    
    public final void onFunction(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void onParentheses() {
    }
    
    public final void onPercent() {
    }
    
    public final void onFactorial() {
    }
    
    public final void onPower() {
    }
    
    public final void onConstant(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void onClear() {
    }
    
    public final void onBackspace() {
    }
    
    public final void onEquals() {
    }
    
    public final void toggleAngleMode() {
    }
    
    public final boolean isDegreeMode() {
        return false;
    }
    
    public final void loadFromHistory(@org.jetbrains.annotations.NotNull()
    com.example.calculator.model.HistoryEntry entry) {
    }
    
    public final void clearHistory() {
    }
    
    private final void updateDisplay() {
    }
    
    private final void evaluateCurrent() {
    }
    
    private final java.lang.String normalizeExpression(java.lang.String input) {
        return null;
    }
}