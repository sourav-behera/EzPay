/**
 * @fileoverview Unit tests for the TransactionStatus model.
 * @version 1.0.0
 */
import { TransactionStatus } from './transaction-status';

describe('TransactionStatus', () => {
  it('should create an instance with provided values', () => {
    // Pass mock data to the constructor to create a valid instance.
    const mockData = new TransactionStatus('TXN12345', 'Debit', 150.00, 'Completed', new Date());
    expect(mockData).toBeTruthy();
  });
});