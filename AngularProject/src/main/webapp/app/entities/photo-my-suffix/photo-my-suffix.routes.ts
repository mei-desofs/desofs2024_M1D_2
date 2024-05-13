import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PhotoMySuffixComponent } from './list/photo-my-suffix.component';
import { PhotoMySuffixDetailComponent } from './detail/photo-my-suffix-detail.component';
import { PhotoMySuffixUpdateComponent } from './update/photo-my-suffix-update.component';
import PhotoMySuffixResolve from './route/photo-my-suffix-routing-resolve.service';

const photoRoute: Routes = [
  {
    path: '',
    component: PhotoMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhotoMySuffixDetailComponent,
    resolve: {
      photo: PhotoMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhotoMySuffixUpdateComponent,
    resolve: {
      photo: PhotoMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhotoMySuffixUpdateComponent,
    resolve: {
      photo: PhotoMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default photoRoute;
