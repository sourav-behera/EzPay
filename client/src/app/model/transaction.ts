import { ITransaction } from "../interface/itransaction";

export class Transaction implements ITransaction {
    transactionId: string;
    type: string;
    amount: number;
    status: string;
    date: Date;
    message?: string;

    constructor(transactionId: string, type: string, amount: number, status: string, date: Date, message?: string) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
        message ? this.message = message : this.message = 'Transaction on ' + date.toLocaleDateString();
    }
}
