import { ITransaction } from "../interface/itransaction";

export class Transaction implements ITransaction {
    transactionId: string;
    type: string;
    amount: number;
    status: string;
    date: Date;

    constructor(transactionId: string, type: string, amount: number, status: string, date: Date) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }
}
