import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoleMySuffix } from '../role-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-role-my-suffix-detail',
  templateUrl: './role-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoleMySuffixDetailComponent {
  role = input<IRoleMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
