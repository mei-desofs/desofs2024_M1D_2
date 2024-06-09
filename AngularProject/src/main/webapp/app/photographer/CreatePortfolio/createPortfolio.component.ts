import { Component, inject, signal, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { UserMySuffixService } from '../../entities/user-my-suffix/service/user-my-suffix.service';
import { PortfolioMySuffixService } from '../../entities/portfolio-my-suffix/service/portfolio-my-suffix.service';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './createPortfolio.component.html',
  styleUrl: './createPortfolio.component.scss',
  imports: [SharedModule, RouterModule, FormsModule],
})
export default class CreatePortfolioComponent implements OnInit {
  account = signal<Account | null>(null);
  portfolioName: string = '';
  private accountService = inject(AccountService);
  private userService = inject(UserMySuffixService);
  private loginService = inject(LoginService);
  private portfolioService = inject(PortfolioMySuffixService);
  private router = inject(Router);

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => this.account.set(account));
  }

  login(): void {
    this.loginService.login();
  }

  onSubmit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.userService.getByEmail(account.email).subscribe(userResponse => {
          if (userResponse.body) {
            const user1 = userResponse.body;
            const newPortfolio = {
              id: null,
              date: dayjs(),
              name: this.portfolioName,
            };

            // Create the portfolio
            this.portfolioService.create(newPortfolio).subscribe(portfolioResponse => {
              if (portfolioResponse.body) {
                const portfolio = portfolioResponse.body;
                user1.portfolio = portfolio;
                this.userService.update(user1).subscribe(user => {
                  this.router.navigate(['/']);
                });
              }
            });
          }
        });
      }
    });
  }
}
