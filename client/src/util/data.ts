import { Transaction } from "../app/model/transaction";

export let transactions: Transaction[] = [
  new Transaction('TRX101', 'UPI', 500, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX102', 'BANK', 600, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX103', 'UPI', 550, 'Pending',   new Date('2023-10-02'), 'Payment for groceries'),
  new Transaction('TRX104', 'BANK', 320, 'Completed', new Date('2023-10-02'), 'Payment for groceries'),
  new Transaction('TRX105', 'UPI', 500, 'Completed', new Date('2023-10-03'), 'Payment for groceries'),
  new Transaction('TRX106', 'BANK', 600, 'Completed', new Date('2023-10-03'), 'Payment for groceries'),
  new Transaction('TRX107', 'UPI', 550, 'Pending',   new Date('2023-10-04'), 'Payment for groceries'),
  new Transaction('TRX108', 'BANK', 320, 'Completed', new Date('2023-10-04'), 'Payment for groceries'),
  new Transaction('TRX109', 'BANK', 320, 'Completed', new Date('2025-08-08'), 'Payment for groceries'),
  new Transaction('TRX110', 'UPI', 1000, 'Pending', new Date('2025-08-08'), 'Payment for groceries'),
  new Transaction('TRX111', 'UPI', 1000, 'Failed', new Date('2025-08-08'), 'Friendly Transfer'),
];