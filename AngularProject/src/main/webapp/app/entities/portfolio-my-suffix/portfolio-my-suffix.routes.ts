import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PortfolioMySuffixComponent } from './list/portfolio-my-suffix.component';
import { PortfolioMySuffixDetailComponent } from './detail/portfolio-my-suffix-detail.component';
import { PortfolioMySuffixUpdateComponent } from './update/portfolio-my-suffix-update.component';
import PortfolioMySuffixResolve from './route/portfolio-my-suffix-routing-resolve.service';

const portfolioRoute: Routes = [
  {
    path: '',
    component: PortfolioMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortfolioMySuffixDetailComponent,
    resolve: {
      portfolio: PortfolioMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortfolioMySuffixUpdateComponent,
    resolve: {
      portfolio: PortfolioMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortfolioMySuffixUpdateComponent,
    resolve: {
      portfolio: PortfolioMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default portfolioRoute;
