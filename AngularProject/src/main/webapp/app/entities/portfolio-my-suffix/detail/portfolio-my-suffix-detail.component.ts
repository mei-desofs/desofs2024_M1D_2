import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-portfolio-my-suffix-detail',
  templateUrl: './portfolio-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PortfolioMySuffixDetailComponent {
  portfolio = input<IPortfolioMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
