package appBank.enums;

public enum TransferenciaType {
    SAQUE(0),
    DEPOSITO(1);

    private final int typeValue;

    TransferenciaType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }
}