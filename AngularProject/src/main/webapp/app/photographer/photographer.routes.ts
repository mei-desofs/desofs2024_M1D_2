import { Routes } from '@angular/router';
import {PhotoMySuffixUpdateComponent} from "../entities/photo-my-suffix/update/photo-my-suffix-update.component";
import PhotoMySuffixResolve from "../entities/photo-my-suffix/route/photo-my-suffix-routing-resolve.service";
import {UserRouteAccessService} from "../core/auth/user-route-access.service";
import {PutUpforSaleComponent} from "./PutUpforSale/putUpforSale.component";
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

const routes: Routes = [
  {
    path: 'create-portfolio',
    loadComponent: () => import('./CreatePortfolio/createPortfolio.component'),
    title: 'Create a Portfolio',
  },
  {
    path: 'view-gallery',
    loadComponent: () => import('./ViewGallery/viewGallery.component'),
    title: 'View Gallery',
  },
  {
    path: 'put-up-for-sale',
    component: PutUpforSaleComponent,
    resolve: {
      photo: PhotoMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },

  /* jhipster-needle-add-admin-route - JHipster will add admin routes here */
];

export default routes;
