/**
 * @fileoverview Component for displaying the transaction history table.
 * @version 1.0.0
 */
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormControl } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';

import { ITransactionStatus } from '../../interface/itransaction-status'; // Use your ITransactionStatus interface
import { TransactionStatus } from '../../model/transaction-status'; // Use your TransactionStatus model

@Component({
  selector: 'app-transaction-status-component',
  standalone: false,
  templateUrl: './transaction-status-component.component.html',
  styleUrls: ['./transaction-status-component.component.css']
})
export class TransactionStatusComponentComponent implements OnInit, AfterViewInit {

  public displayedColumns: string[] = ['icon', 'info', 'amount'];
  public dataSource = new MatTableDataSource<ITransactionStatus>();
  public isLoadingResults = true;
  public filterTerm = new FormControl('');
  public selectedStatusType = new FormControl('All');

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  public statusTypes: string[] = ['All', 'Completed', 'Processing', 'Failed'];

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    // Register custom SVG icons for the status types.
    const getSvg = (color: string) =>
  `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" fill="${color}"/></svg>`;
    this.matIconRegistry.addSvgIconLiteral('completed-status', this.domSanitizer.bypassSecurityTrustHtml(getSvg('#34C759')));
    this.matIconRegistry.addSvgIconLiteral('processing-status', this.domSanitizer.bypassSecurityTrustHtml(getSvg('#FF9500')));
    this.matIconRegistry.addSvgIconLiteral('failed-status', this.domSanitizer.bypassSecurityTrustHtml(getSvg('#FF3B30')));
  }

  ngOnInit(): void {
    this.fetchTransactionHistory();
    this.filterTerm.valueChanges.subscribe(() => this.applyFilter());
    this.selectedStatusType.valueChanges.subscribe(() => this.applyFilter());
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  private fetchTransactionHistory(): void {
    this.isLoadingResults = true;
    
    // Using mock data directly as requested
    const mockData: ITransactionStatus[] = [
      new TransactionStatus('TXN789012', 'Debit', -250.00, 'Completed', new Date('2025-08-08T14:30:00')),
      new TransactionStatus('TXN789011', 'Debit', -89.45, 'Completed', new Date('2025-08-07T10:15:00')),
      new TransactionStatus('TXN789010', 'Debit', -500.00, 'Processing', new Date('2025-08-07T09:45:00')),
      new TransactionStatus('TXN789009', 'Credit', 3500.00, 'Completed', new Date('2024-12-28T16:00:00')),
      new TransactionStatus('TXN789008', 'Debit', -45.99, 'Failed', new Date('2024-12-27T18:20:00'))
    ];
    
    // Simulate a service call by using setTimeout
    setTimeout(() => {
      this.dataSource.data = mockData;
      this.setupCustomFilter();
      this.isLoadingResults = false;
    }, 1000); // 1-second delay
  }

  public applyFilter(): void {
    const searchTerm = this.filterTerm.value ? this.filterTerm.value.trim().toLowerCase() : '';
    const statusTypeFilter = this.selectedStatusType.value;
    this.dataSource.filter = JSON.stringify({ searchTerm, statusTypeFilter });

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private setupCustomFilter(): void {
    this.dataSource.filterPredicate = (data: ITransactionStatus, filter: string): boolean => {
      const { searchTerm, statusTypeFilter } = JSON.parse(filter);
      const statusTypeMatch = statusTypeFilter === 'All' || data.status.toLowerCase() === statusTypeFilter.toLowerCase();
      const globalMatch = 
        data.type.toLowerCase().includes(searchTerm) ||
        data.transactionId.toLowerCase().includes(searchTerm);
      return globalMatch && statusTypeMatch;
    };
  }

  public getStatusIcon(statusType: string): string {
    switch (statusType) {
      case 'Completed':
        return 'check_circle';
      case 'Processing':
        return 'autorenew';
      case 'Failed':
        return 'cancel';
      default:
        return 'info';
    }
  }
} 