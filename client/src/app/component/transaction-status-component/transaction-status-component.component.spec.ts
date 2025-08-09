import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionStatusComponentComponent } from './transaction-status-component.component';

describe('TransactionStatusComponentComponent', () => {
  let component: TransactionStatusComponentComponent;
  let fixture: ComponentFixture<TransactionStatusComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TransactionStatusComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionStatusComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
