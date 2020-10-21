package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.LinkResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface LinkService {
    LinkResponse retrieveURLFromDB(String slug) throws SQLException;
    LinkResponse writeURLToDBForPaid(LinkRequest request, HttpServletRequest httpServletRequest) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForFree(LinkRequest request, HttpServletRequest httpServletRequest) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForIncognito(LinkRequest request) throws SQLException, BadRequestException;
}
