package com.example.calculator.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/calculator/model/CalculatorDatabase;", "Landroidx/room/RoomDatabase;", "()V", "historyDao", "Lcom/example/calculator/model/HistoryDao;", "Companion", "app-calculator_debug"})
@androidx.room.Database(entities = {com.example.calculator.model.HistoryEntry.class}, version = 1, exportSchema = false)
public abstract class CalculatorDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.calculator.model.CalculatorDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calculator.model.CalculatorDatabase.Companion Companion = null;
    
    public CalculatorDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.calculator.model.HistoryDao historyDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/calculator/model/CalculatorDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/calculator/model/CalculatorDatabase;", "getInstance", "context", "Landroid/content/Context;", "app-calculator_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calculator.model.CalculatorDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}