import { Component } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { TransactionDTO } from './models/account.model';
import { BankAccountService } from './services/account.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  imports: [MatButtonModule, MatCardModule, MatInputModule, MatTableModule, MatIcon, CurrencyPipe, DatePipe, FormsModule,],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'bankaccount-frontend';
  accountId: number = 1
  amount = 0;
  balance = 0;
  transactions: TransactionDTO[] = [];
  displayedColumns = ['date', 'amount', 'balance'];

  constructor(
    private bankAccountService: BankAccountService,
    private snackBar: MatSnackBar
  ) {
    this.loadStatement();
  }

  deposit() {
    this.bankAccountService.deposit(this.accountId, this.amount).subscribe({
      next: (res) => {
        this.balance = res.balance;
        this.transactions = res.transactions;
        this.snackBar.open('Deposit successful!', 'Close', { duration: 3000 });
      },
      error: (err) => this.snackBar.open(`Error: ${err.error?.message}`, 'Close'),
    });
  }

  withdraw() {
    this.bankAccountService.withdraw(this.accountId, this.amount).subscribe({
      next: (res) => {
        this.balance = res.balance;
        this.transactions = res.transactions;
        this.snackBar.open('Withdraw successful!', 'Close', { duration: 3000 });
      },
      error: (err) => this.snackBar.open(`Error: ${err.error?.message}`, 'Close'),
    });
  }

  private loadStatement() {
    this.bankAccountService.getStatement(this.accountId).subscribe((res) => {
      this.balance = res.balance;
      this.transactions = res.transactions;
    });
  }
}
