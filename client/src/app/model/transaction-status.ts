/**
 * @fileoverview Defines the TransactionStatus model class.
 * @version 1.0.1
 */
import { ITransactionStatus } from "../interface/itransaction-status";

export class TransactionStatus implements ITransactionStatus {
  transactionId: string;
  type: string;
  amount: number;
  status: string;
  date: Date;
  from: string;
  to: string;

  constructor(
    transactionId: string,
    type: string,
    amount: number,
    status: string,
    date: Date,
    from: string, // Added 'from' to constructor
    to: string // Added 'to' to constructor
  ) {
    this.transactionId = transactionId;
    this.type = type;
    this.amount = amount;
    this.status = status;
    this.date = date;
    this.from = from;
    this.to = to;
  }
}

