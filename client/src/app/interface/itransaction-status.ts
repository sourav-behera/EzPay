import { ITransaction } from "./itransaction";

export interface ITransactionStatus extends ITransaction{
    transactionId: string;
    type: string;
    amount: number;
    status: string;
    date: Date;
}