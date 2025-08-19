export interface ITransactionStatus{
  transactionId: string;
  type: string;
  amount: number;
  status: string;
  date: Date;
  from: string; // Added 'from' property
  to: string; // Added 'to' property
}