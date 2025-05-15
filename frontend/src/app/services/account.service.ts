import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccountDTO } from '../models/account.model';

@Injectable({ providedIn: 'root' })
export class BankAccountService {
    private readonly API_URL = 'http://localhost:8080/api/account';

    constructor(private http: HttpClient) { }

    deposit(accountId: number, amount: number): Observable<BankAccountDTO> {
        return this.http.post<BankAccountDTO>(`${this.API_URL}/deposit?accountId=${accountId}&amount=${amount}`, {});
    }

    withdraw(accountId: number, amount: number): Observable<BankAccountDTO> {
        return this.http.post<BankAccountDTO>(`${this.API_URL}/withdraw?accountId=${accountId}&amount=${amount}`, {});
    }

    getStatement(accountId: number): Observable<BankAccountDTO> {
        return this.http.get<BankAccountDTO>(`${this.API_URL}/statement?accountId=${accountId}`);
    }
}