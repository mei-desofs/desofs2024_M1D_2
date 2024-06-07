import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import {FormsModule} from "@angular/forms";

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './createPortfolio.component.html',
  styleUrl: './createPortfolio.component.scss',
  imports: [SharedModule, RouterModule, FormsModule],
})
export default class CreatePortfolioComponent implements OnInit {
  account = signal<Account | null>(null);
  portfolioName : string = '';
  private accountService = inject(AccountService);
  private loginService = inject(LoginService);

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => this.account.set(account));
  }

  login(): void {
    this.loginService.login();
  }
}
