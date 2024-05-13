import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { PortfolioMySuffixService } from '../service/portfolio-my-suffix.service';

const portfolioResolve = (route: ActivatedRouteSnapshot): Observable<null | IPortfolioMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(PortfolioMySuffixService)
      .find(id)
      .pipe(
        mergeMap((portfolio: HttpResponse<IPortfolioMySuffix>) => {
          if (portfolio.body) {
            return of(portfolio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default portfolioResolve;
