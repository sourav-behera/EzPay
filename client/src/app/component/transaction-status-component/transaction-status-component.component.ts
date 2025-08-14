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
import { ITransactionStatus } from '../../interface/itransaction-status';
import { TransactionStatusService } from '../../services/transaction-status.service';
import { TransactionStatus } from '../../model/transaction-status';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-transaction-status-component',
  standalone: false,
  templateUrl: './transaction-status-component.component.html',
  styleUrls: ['./transaction-status-component.component.css'],
})
export class TransactionStatusComponentComponent
  implements OnInit, AfterViewInit {
  /** Table columns */
  public displayedColumns: string[] = [
    'icon',
    'info',
    'amount',
    'status',
    'action',
  ];
  public dataSource = new MatTableDataSource<ITransactionStatus>();

  /** UI state */
  public isLoadingResults = true;
  public filterTerm = new FormControl('');
  public selectedStatusType = new FormControl('All');

  /** Holds selected transaction details for modal display */
  public selectedTransaction: ITransactionStatus | null = null;

  /** Pagination & sorting */
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  /** Status filter types */
  public statusTypes: string[] = ['All', 'Completed', 'Processing', 'Failed'];

  mockData: TransactionStatus[] = [];

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private transactionStatusService: TransactionStatusService
  ) {
    // Register custom SVG icons for the status types.
    const getSvg = (color: string) =>
      `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" fill="${color}"/></svg>`;
    this.matIconRegistry.addSvgIconLiteral(
      'completed-status',
      this.domSanitizer.bypassSecurityTrustHtml(getSvg('#34C759'))
    );
    this.matIconRegistry.addSvgIconLiteral(
      'processing-status',
      this.domSanitizer.bypassSecurityTrustHtml(getSvg('#FF9500'))
    );
    this.matIconRegistry.addSvgIconLiteral(
      'failed-status',
      this.domSanitizer.bypassSecurityTrustHtml(getSvg('#FF3B30'))
    );
  }



  ngOnInit(): void {
    this.fetchTransactionHistory();
    this.filterTerm.valueChanges.subscribe(() => this.applyFilter());
    this.selectedStatusType.valueChanges.subscribe(() => this.applyFilter());
    // Fetch transaction data from the service
    this.transactionStatusService.getData$().subscribe((data: TransactionStatus[]) => {
      this.mockData = data;
    });

  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  /** Loads mock transaction history with more details */
  private fetchTransactionHistory(): void {
    this.isLoadingResults = true;
    


    setTimeout(() => {
      this.dataSource.data = this.mockData;
      this.setupCustomFilter();
      this.isLoadingResults = false;
    }, 1000); // 1-second delay
  }

  /** Applies search + status filter */
  public applyFilter(): void {
    const searchTerm = this.filterTerm.value
      ? this.filterTerm.value.trim().toLowerCase()
      : '';
    const statusTypeFilter = this.selectedStatusType.value;
    this.dataSource.filter = JSON.stringify({ searchTerm, statusTypeFilter });

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** Custom filter for both search & dropdown filter */
  private setupCustomFilter(): void {
    this.dataSource.filterPredicate = (
      data: ITransactionStatus,
      filter: string
    ): boolean => {
      const { searchTerm, statusTypeFilter } = JSON.parse(filter);
      const statusTypeMatch =
        statusTypeFilter === 'All' ||
        data.status.toLowerCase() === statusTypeFilter.toLowerCase();
      const globalMatch =
        data.type.toLowerCase().includes(searchTerm) ||
        data.transactionId.toLowerCase().includes(searchTerm) ||
        data.from.toLowerCase().includes(searchTerm) ||
        data.to.toLowerCase().includes(searchTerm);
      return globalMatch && statusTypeMatch;
    };
  }

  /** Returns icon name based on status */
  public getStatusIcon(statusType: string): string {
    switch (statusType) {
      case 'Completed':
        return 'completed-status';
      case 'Processing':
        return 'processing-status';
      case 'Failed':
        return 'failed-status';
      default:
        return 'info';
    }
  }

  /** Opens the transaction details modal */
  public openTransactionDetails(transaction: ITransactionStatus): void {
    this.selectedTransaction = transaction;
  }

  /** Closes the transaction details modal */
  public closeTransactionDetails(): void {
    this.selectedTransaction = null;
  }

  public showPaymentDetails = false;

  copy(text: string) {
    if (!text) return;
    navigator.clipboard?.writeText(text);
  }
}
