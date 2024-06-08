import { Component, inject, signal, OnInit } from '@angular/core';
import {Router, RouterModule} from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import {FormsModule} from "@angular/forms";
import {Observable} from "rxjs";

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './viewPurchasedPhotos.component.html',
  styleUrl: './viewPurchasedPhotos.component.scss',
  imports: [SharedModule, RouterModule, FormsModule],
})
export default class ViewPurchasedPhotosComponent implements OnInit {
  account = signal<Account | null>(null);
  portfolioName : string = '';
  private accountService = inject(AccountService);
  private loginService = inject(LoginService);
  private router = inject(Router);

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => this.account.set(account));
  }

  login(): void {
    this.loginService.login();
  }
  onSubmit(): void {
    this.accountService.identity().subscribe(account => {
      console.log("boas");
      if (account) {
        console.log(this.portfolioName);
        console.log(account.login);
        console.log(account.email);

      }
    });
  }
}
