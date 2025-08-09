export interface ITransaction {
    transactionId: string;
    type: string;
    amount: number;
    status: string;
    date: Date;
}