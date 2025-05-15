export interface BankAccountDTO {
    balance: number;
    transactions: TransactionDTO[];
}

export interface TransactionDTO {
    date: string;
    amount: string;
    balanceAfterTransaction: number;
}