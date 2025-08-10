import { ITransaction } from "../interface/itransaction";

export class Transaction implements ITransaction {
    // private _transactionId: string;
    // private _type: string;
    // private _amount: number;
    // private _status: string;
    // private _date: Date;
    // private _message?: string;
    
    constructor(
        private _transactionId: string,
        private _type: string,
        private _amount: number,
        private _status: string,
        private _date: Date,
        private _message?: string
    ) {}
    
    public get transactionId(): string {
        return this._transactionId;
    }

    public get type(): string {
        return this._type;
    }

    public get amount(): number {
        return this._amount;
    }

    public get status(): string {
        return this._status;
    }

    public get date(): Date {
        return this._date;
    }
    
    public get message(): string {
        return this._message || `Transaction on ${this._date.toLocaleDateString()}`;
    }
    
    public set type(newType: string) {
        this._type = newType;
    }

    public set amount(newAmount: number) {
        if (newAmount < 0) {
            console.error("Amount cannot be negative.");
            return; // Or throw an error
        }
        this._amount = newAmount;
    }

    public set status(newStatus: string) {
        this._status = newStatus;
    }
    
    public set message(newMessage: string) {
        this._message = newMessage.trim();
    }

    // constructor(transactionId: string, type: string, amount: number, status: string, date: Date, message?: string) {
    //     this._transactionId = transactionId;
    //     this._type = type;
    //     this._amount = amount;
    //     this._status = status;
    //     this._date = date;
    //     message ? this._message = message : this._message = 'Transaction on ' + date.toLocaleDateString();
    // }
}
