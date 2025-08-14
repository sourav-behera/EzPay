import { TransactionStatus } from '../model/transaction-status';

export const transactionsList: TransactionStatus[] = [
  new TransactionStatus(
        'TXN789012',
        'Debit',
        -250.0,
        'Completed',
        new Date('2025-08-08T14:30:00'),
        'Alice',
        'Netflix'
      ),
      new TransactionStatus(
        'TXN789011',
        'Debit',
        -89.45,
        'Completed',
        new Date('2025-08-07T10:15:00'),
        'Alice',
        'Amazon'
      ),
      new TransactionStatus(
        'TXN789010',
        'Debit',
        -500.0,
        'Processing',
        new Date('2025-08-07T09:45:00'),
        'Alice',
        'Car Insurance'
      ),
      new TransactionStatus(
        'TXN789009',
        'Credit',
        3500.0,
        'Completed',
        new Date('2024-12-28T16:00:00'),
        'Company Ltd',
        'Alice'
      ),
      new TransactionStatus(
        'TXN789008',
        'Debit',
        -45.99,
        'Failed',
        new Date('2024-12-27T18:20:00'),
        'Alice',
        'Spotify'
      ),
];
