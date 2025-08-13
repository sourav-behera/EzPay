import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { provideNativeDateAdapter } from '@angular/material/core';
import { Transaction } from '../../model/transaction';
import { PageEvent } from '@angular/material/paginator';
import { TransactionService } from '../../service/transaction.service';


@Component({
  selector: 'app-transaction-list',
  standalone: false,
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TransactionListComponent implements OnInit{
  
  pagedData: Transaction[] = []; // Data for the view
  private filteredData: Transaction[] = []; // Holds all data after filtering/sorting from the service

  // UI state for filters
  selectedFilter: string = '';
  fromDate: Date | null = null;
  toDate: Date | null = null;
  minimumAmount: number | null = null;
  maximumAmount: number | null = null;
  
  // Pagination state
  pageSize: number = 10;
  currentPage: number = 0;
  
  constructor(private transactionService: TransactionService) {}
  
  ngOnInit(): void {
    this.refreshData();
  }
  
  public get totalDataLength(): number {
    return this.filteredData.length;
  }
  
  // --- Event Handlers ---
  
  public handleSorting(sortBy: string): void {
    this.transactionService.sortData(sortBy);
    this.refreshData();
  }
  
  public getTransactionsByType(type: string): void {
    this.transactionService.filterByType(type);
    this.refreshData();
  }
  
  public getTransactionsByStatus(status: string): void {
    this.transactionService.filterByStatus(status);
    this.refreshData();
  }
  
  public getTransactionsByAmountRange(): void {
    this.transactionService.filterByAmountRange(this.minimumAmount, this.maximumAmount);
    this.refreshData();
  }
  
  public getTransactionsByDateRange(): void {
    this.transactionService.filterByDateRange(this.fromDate, this.toDate);
    this.refreshData();
  }
  
  public getTransactionByDate(selectedDate: Date): void {
    this.transactionService.filterByDate(selectedDate);
    this.refreshData();
  }
  
  public getTransactionsBySearchID(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.transactionService.filterBySearchID(filterValue);
    this.refreshData();
  }

  public handlePageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePagedData();
  }
  
  
  // --- UI Update Logic ---

  /**
   * Fetches the latest data from the service and updates the component's view.
   */
  private refreshData(): void {
    this.filteredData = this.transactionService.getTransactions();
    this.currentPage = 0; // Reset to the first page whenever data changes
    this.updatePagedData();
  }

  public updatePagedData(): void {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.pagedData = this.filteredData.slice(startIndex, endIndex);
  }
  
  public resetForm(): void {
    // Reset component's local UI state
    this.selectedFilter = '';
    this.fromDate = null;
    this.toDate = null;
    this.minimumAmount = null;
    this.maximumAmount = null;

    this.transactionService.resetFilters();
    this.refreshData();
  }
}
