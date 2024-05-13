import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoleUserMySuffix } from '../role-user-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-role-user-my-suffix-detail',
  templateUrl: './role-user-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoleUserMySuffixDetailComponent {
  roleUser = input<IRoleUserMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
